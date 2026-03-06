import api from "@/lib/api"

export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  token: string
  user: {
    id: string
    email: string
    name: string
  }
}

export const authService = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>("/auth/login", data)
    return response.data
  },

  logout: async (): Promise<void> => {
    await api.post("/auth/logout")
    localStorage.removeItem("token")
  },

  getCurrentUser: async () => {
    const response = await api.get("/auth/me")
    return response.data
  },
}
