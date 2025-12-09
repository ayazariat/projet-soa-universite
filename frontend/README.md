# University Management System - Frontend

A modern, professional React-based frontend application for the university management system built with cutting-edge technologies.

## 🚀 Tech Stack

- **React 19** - Latest React with improved performance
- **TypeScript** - Type-safe development
- **Vite** - Lightning-fast build tool
- **Tailwind CSS v4** - Modern utility-first CSS framework
- **shadcn/ui** - Beautiful, accessible component library
- **React Router v7** - Declarative routing
- **TanStack Query** - Powerful data fetching and caching
- **Zustand** - Lightweight state management
- **Lucide React** - Beautiful icon library

## 📦 Installation

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Install missing peer dependencies (if needed):
```bash
npm install @radix-ui/react-dropdown-menu zustand
```

## 🏃 Running the Application

### Development Mode
```bash
npm run dev
```
The app will be available at `http://localhost:5173`

### Production Build
```bash
npm run build
npm run preview
```

## 🔑 Features

### Authentication
- ✅ User login with JWT tokens
- ✅ User registration with role selection (Admin, Teacher, Student)
- ✅ Secure token storage and management
- ✅ Protected routes with authentication guards
- ✅ Password validation with strong requirements

### Dashboard
- ✅ Role-based dashboard views
- ✅ Statistics and metrics cards
- ✅ Recent activity feed
- ✅ Quick action shortcuts
- ✅ Responsive sidebar navigation

### User Interface
- ✅ Modern, clean design with professional color scheme
- ✅ Fully responsive layout (mobile, tablet, desktop)
- ✅ Dark mode support (via Tailwind)
- ✅ Accessible components (WCAG compliant)
- ✅ Smooth animations and transitions

## 📁 Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── auth/           # Authentication components
│   │   ├── layout/         # Layout components (Sidebar, Header)
│   │   └── ui/             # shadcn UI components
│   ├── pages/              # Page components
│   ├── lib/                # Utilities and API client
│   ├── store/              # Zustand state management
│   ├── App.tsx             # Main app component with routing
│   ├── main.tsx            # Entry point
│   └── index.css           # Global styles and Tailwind
├── public/                 # Static assets
└── package.json
```

## 🔌 API Integration

The frontend connects to the Spring Boot backend running on `http://localhost:8081`.

### API Endpoints Used:
- `POST /api/auth/login` - User authentication
- `POST /api/auth/register` - User registration
- `GET /api/auth/me` - Get current user info
- `POST /api/auth/logout` - User logout
- `POST /api/auth/update-password` - Update password

### Proxy Configuration
Vite is configured to proxy API requests:
```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    }
  }
}
```

## 🎨 Customization

### Theme Colors
Edit `src/index.css` to customize the color scheme:
```css
:root {
  --primary: oklch(0.52 0.18 264);  /* Primary brand color */
  --secondary: oklch(0.95 0.01 264); /* Secondary color */
  --accent: oklch(0.95 0.02 300);    /* Accent color */
  /* ... more colors */
}
```

### Adding Components
Use shadcn CLI to add more components:
```bash
npx shadcn@latest add [component-name]
```

## 🔐 Authentication Flow

1. User enters credentials on login page
2. Frontend sends POST request to `/api/auth/login`
3. Backend validates and returns JWT token
4. Token stored in localStorage and Zustand store
5. Token included in Authorization header for protected requests
6. Protected routes check authentication status
7. Logout clears token and redirects to login

## 🚧 Upcoming Features

- [ ] Course management module
- [ ] Student enrollment system
- [ ] Teacher assignment interface
- [ ] Grade management
- [ ] Attendance tracking
- [ ] Notification system
- [ ] File upload/download
- [ ] Real-time updates with WebSocket

## 📱 Responsive Design

The application is fully responsive with breakpoints:
- Mobile: < 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px

## 🛠️ Development Guidelines

### Code Style
- Use TypeScript for type safety
- Follow React best practices
- Use functional components with hooks
- Implement proper error handling
- Write meaningful commit messages

### Component Guidelines
- Keep components small and focused
- Use shadcn components when possible
- Extract reusable logic into custom hooks
- Implement proper loading and error states

## 📄 License

This project is part of the SOA University Management System.

## 👥 Contributors

- 3ème Licence GL/SI Team

## 🆘 Support

For issues or questions, please contact the development team or create an issue in the repository.