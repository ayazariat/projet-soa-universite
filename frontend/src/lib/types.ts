export type Role = 'ADMIN' | 'TEACHER' | 'STUDENT';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: Role;
}

export interface AuthResponse {
  token: string;
  type: string;
  expiresIn: number;
  user: UserDTO;
}

export interface UserDTO {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
}

export interface UpdatePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string | null;
}

// Student types
export interface Student {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  studentId: string;
  dateOfBirth?: string;
  enrollmentDate: string;
  major?: string;
  gpa?: number;
  status: 'ACTIVE' | 'INACTIVE' | 'GRADUATED';
}

export interface CreateStudentRequest {
  firstName: string;
  lastName: string;
  email: string;
  studentId: string;
  dateOfBirth?: string;
  major?: string;
}

export interface UpdateStudentRequest extends Partial<CreateStudentRequest> {
  status?: Student['status'];
}

// Course types
export interface Course {
  id: string;
  code: string;
  name: string;
  description?: string;
  credits: number;
  instructor?: string;
  schedule?: string;
  capacity: number;
  enrolled: number;
  semester: string;
  status: 'ACTIVE' | 'CANCELLED' | 'COMPLETED';
}

export interface CreateCourseRequest {
  code: string;
  name: string;
  description?: string;
  credits: number;
  instructor?: string;
  schedule?: string;
  capacity: number;
  semester: string;
}

export interface UpdateCourseRequest extends Partial<CreateCourseRequest> {
  status?: Course['status'];
}