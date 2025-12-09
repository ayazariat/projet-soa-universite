import type { LoginRequest, RegisterRequest, AuthResponse, UserDTO, UpdatePasswordRequest, ErrorResponse } from './types';

const API_BASE = '/api';

class ApiClient {
  private getHeaders(includeAuth = true): HeadersInit {
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
    };

    if (includeAuth) {
      const token = localStorage.getItem('auth_token');
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
    }

    return headers;
  }

  async request<T>(
    endpoint: string,
    options: RequestInit = {},
    includeAuth = true
  ): Promise<T> {
    const response = await fetch(`${API_BASE}${endpoint}`, {
      ...options,
      headers: {
        ...this.getHeaders(includeAuth),
        ...options.headers,
      },
    });

    if (!response.ok) {
      let errorMessage = `HTTP ${response.status}`;
      const errorText = await response.text();
      try {
        const errorData: ErrorResponse = JSON.parse(errorText);
        if (errorData.message) {
          errorMessage = errorData.message;
        }
      } catch {
        // If not JSON, use the text directly
        if (errorText) {
          errorMessage = errorText;
        }
      }
      throw new Error(errorMessage);
    }

    const contentType = response.headers.get('content-type');
    if (contentType?.includes('application/json')) {
      return response.json();
    }

    return response.text() as T;
  }

  // Auth endpoints
  async login(data: LoginRequest): Promise<AuthResponse> {
    return this.request<AuthResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    }, false);
  }

  async register(data: RegisterRequest): Promise<UserDTO> {
    return this.request<UserDTO>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    }, false);
  }

  async getCurrentUser(): Promise<UserDTO> {
    return this.request<UserDTO>('/auth/me');
  }

  async logout(): Promise<void> {
    return this.request<void>('/auth/logout', {
      method: 'POST',
    });
  }

  async updatePassword(data: UpdatePasswordRequest): Promise<string> {
    return this.request<string>('/auth/update-password', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }
}

export const apiClient = new ApiClient();