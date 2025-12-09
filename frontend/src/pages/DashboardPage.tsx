import { useAuthStore } from '@/store/authStore';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { BookOpen, Users, GraduationCap, TrendingUp } from 'lucide-react';

export function DashboardPage() {
  const user = useAuthStore((state) => state.user);

  const stats = [
    {
      title: 'Total Courses',
      value: '12',
      description: 'Active courses',
      icon: BookOpen,
      trend: '+2 from last semester',
    },
    {
      title: 'Students Enrolled',
      value: '1,234',
      description: 'Total students',
      icon: Users,
      trend: '+15% from last year',
    },
    {
      title: 'Teachers',
      value: '45',
      description: 'Faculty members',
      icon: GraduationCap,
      trend: '+3 new hires',
    },
    {
      title: 'Average Grade',
      value: '85%',
      description: 'Overall performance',
      icon: TrendingUp,
      trend: '+5% improvement',
    },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">
          Welcome back, {user?.firstName}!
        </h1>
        <p className="text-muted-foreground">
          Here's what's happening with your university today.
        </p>
      </div>

      <div className="flex items-center gap-2">
        <Badge variant="secondary">{user?.role}</Badge>
        <Badge variant="outline">{user?.email}</Badge>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <Card key={stat.title}>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">{stat.title}</CardTitle>
                <Icon className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{stat.value}</div>
                <p className="text-xs text-muted-foreground mt-1">{stat.description}</p>
                <p className="text-xs text-primary mt-2">{stat.trend}</p>
              </CardContent>
            </Card>
          );
        })}
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
            <CardDescription>Your latest actions and updates</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex items-center gap-4">
                <div className="h-2 w-2 rounded-full bg-primary" />
                <div className="flex-1">
                  <p className="text-sm font-medium">Course enrollment opened</p>
                  <p className="text-xs text-muted-foreground">2 hours ago</p>
                </div>
              </div>
              <div className="flex items-center gap-4">
                <div className="h-2 w-2 rounded-full bg-primary" />
                <div className="flex-1">
                  <p className="text-sm font-medium">New assignment posted</p>
                  <p className="text-xs text-muted-foreground">5 hours ago</p>
                </div>
              </div>
              <div className="flex items-center gap-4">
                <div className="h-2 w-2 rounded-full bg-muted" />
                <div className="flex-1">
                  <p className="text-sm font-medium">Grade submission deadline</p>
                  <p className="text-xs text-muted-foreground">1 day ago</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
            <CardDescription>Common tasks and shortcuts</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              <button className="w-full text-left px-4 py-2 rounded-lg hover:bg-accent transition-colors">
                <p className="text-sm font-medium">View All Courses</p>
                <p className="text-xs text-muted-foreground">Browse course catalog</p>
              </button>
              <button className="w-full text-left px-4 py-2 rounded-lg hover:bg-accent transition-colors">
                <p className="text-sm font-medium">Manage Students</p>
                <p className="text-xs text-muted-foreground">View and edit student records</p>
              </button>
              <button className="w-full text-left px-4 py-2 rounded-lg hover:bg-accent transition-colors">
                <p className="text-sm font-medium">Generate Reports</p>
                <p className="text-xs text-muted-foreground">Create performance reports</p>
              </button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}