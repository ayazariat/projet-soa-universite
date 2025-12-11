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
  _id?: string;
  nom: string;
  prenom: string;
  cin: string;
  email: string;
  telephone: string;
  niveau: string;
  genre: 'Masculin' | 'Féminin';
  dateDeNaissance: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateStudentRequest {
  nom: string;
  prenom: string;
  cin: string;
  email: string;
  telephone: string;
  niveau: string;
  genre: 'Masculin' | 'Féminin';
  dateDeNaissance: string;
}

export interface UpdateStudentRequest extends Partial<CreateStudentRequest> {}

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
  status: string;
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

export interface UpdateCourseRequest extends Partial<CreateCourseRequest> {}