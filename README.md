# Projet SOA - Gestion Universitaire (3ème Licence GL/SI)

A comprehensive university management system built with microservices architecture.

## 🏗️ Architecture

This project follows a microservices architecture with:
- **Backend**: Spring Boot microservices (Java 21)
- **Frontend**: Modern React application (TypeScript)
- **Database**: MongoDB for authentication service

## 📦 Project Structure

```
projet-soa-universite/
├── services/
│   ├── auth-service/        # Authentication & Authorization Service
│   ├── course-service/      # Course Management Service (Coming Soon)
│   └── student-service/     # Student Management Service (Coming Soon)
├── frontend/                # React Frontend Application
├── docker/                  # Docker configurations
├── documentation/           # Project documentation
└── presentations/           # Project presentations
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Node.js 20.19+** or **22.12+**
- **MongoDB** (for auth-service)
- **Maven** (for building Java services)
- **npm** or **yarn** (for frontend)

### Backend Setup

1. **Start MongoDB**:
```bash
# Using Docker
docker run -d -p 27017:27017 --name mongodb2 mongo:latest

# Or use your local MongoDB installation
```

2. **Run Auth Service**:
```bash
cd services/auth-service/auth-service
mvn spring-boot:run
```

The auth service will be available at `http://localhost:8081/api`

### Frontend Setup

1. **Install Dependencies**:
```bash
cd frontend
npm install
```

2. **Install Additional Dependencies** (if needed):
```bash
npm install @radix-ui/react-dropdown-menu zustand
```

3. **Run Development Server**:
```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

## 🔑 Default Credentials

After registration, you can login with your created account. The system supports three roles:
- **ADMIN** - Full system access
- **TEACHER** - Course and student management
- **STUDENT** - View courses and grades

## 📚 Services Documentation

### Auth Service
- **Port**: 8081
- **Context Path**: `/api`
- **Documentation**: See [services/auth-service/DOCUMENTATION.md](services/auth-service/DOCUMENTATION.md)
- **Architecture**: See [services/auth-service/ARCHITECTURE.md](services/auth-service/ARCHITECTURE.md)

### Frontend Application
- **Port**: 5173 (development)
- **Documentation**: See [frontend/README.md](frontend/README.md)
- **Tech Stack**: React 19, TypeScript, Vite, Tailwind v4, shadcn/ui

## 🛠️ Technology Stack

### Backend
- Spring Boot 3.3.0
- Spring Security with JWT
- Spring Data MongoDB
- Java 21
- Maven

### Frontend
- React 19
- TypeScript
- Vite
- Tailwind CSS v4
- shadcn/ui Components
- React Router v7
- TanStack Query
- Zustand

### Database
- MongoDB

## 📖 API Documentation

### Authentication Endpoints

**Base URL**: `http://localhost:8081/api/auth`

#### Register
```http
POST /register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john.doe@university.edu",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT"
}
```

#### Login
```http
POST /login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePass123!"
}
```

#### Get Current User
```http
GET /me
Authorization: Bearer <token>
```

#### Logout
```http
POST /logout
Authorization: Bearer <token>
```

## 🐳 Docker Support

Docker configurations are available in the `docker/` directory (Coming Soon).

## 📝 Development Guidelines

### Backend Development
- Follow Spring Boot best practices
- Use proper exception handling
- Implement comprehensive logging
- Write unit and integration tests

### Frontend Development
- Use TypeScript for type safety
- Follow React best practices
- Implement proper error handling
- Ensure responsive design

## 🔒 Security

- JWT-based authentication
- BCrypt password hashing
- CORS configuration
- Role-based access control (RBAC)
- Input validation
- Secure token storage

## 🚧 Roadmap

- [x] Authentication Service
- [x] Frontend Application
- [ ] Course Management Service
- [ ] Student Management Service
- [ ] Grade Management
- [ ] Attendance Tracking
- [ ] Notification System
- [ ] API Gateway
- [ ] Service Discovery
- [ ] Docker Compose Setup

## 👥 Team

3ème Licence GL/SI

## 📄 License

This project is developed as part of the SOA course curriculum.

## 🆘 Support

For issues or questions:
1. Check the documentation in each service directory
2. Review the architecture diagrams
3. Contact the development team

---

**Note**: This is an educational project for learning microservices architecture and modern web development practices.