import api from "@/lib/api"

export interface User {
  id: number
  userName: string
  userEmail: string
  userAddress: string
  phoneNumber: string
  role: string
  status: "ACTIVE" | "INACTIVE"
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: string
}

export interface UserFilterResponse {
  content: User[]
  pageNumber: number
  pageSize: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface GetUsersParams {
  page?: number
  size?: number
  sortBy?: string
  direction?: "asc" | "desc"
  userName?: string
  userEmail?: string
  status?: string
  phoneNumber?: string
}

export const userService = {
  getUsers: async (params?: GetUsersParams): Promise<UserFilterResponse> => {
    const response = await api.get<ApiResponse<UserFilterResponse>>("/users/filter", { params })
    return response.data.data
  },

  getUser: async (id: number): Promise<User> => {
    const response = await api.get<ApiResponse<User>>(`/users/${id}`)
    return response.data.data
  },

  createUser: async (data: Omit<User, "id">): Promise<User> => {
    const response = await api.post<ApiResponse<User>>("/users", data)
    return response.data.data
  },

  updateUser: async (id: number, data: Partial<User>): Promise<User> => {
    const response = await api.put<ApiResponse<User>>(`/users/${id}`, data)
    return response.data.data
  },

  deleteUser: async (id: number): Promise<void> => {
    await api.delete(`/users/${id}`)
  },
}
