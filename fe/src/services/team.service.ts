import api from "@/lib/api"

export interface Team {
  id: number
  teamName: string
  description?: string
}

export interface CreateTeamRequest {
  teamName: string
  description?: string
}

export interface UpdateTeamRequest {
  teamName?: string
  description?: string
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: string
}

export const teamService = {
  getAllTeams: async (): Promise<Team[]> => {
    const response = await api.get<ApiResponse<Team[]>>("/teams")
    return response.data.data
  },

  getTeam: async (id: number): Promise<Team> => {
    const response = await api.get<ApiResponse<Team>>(`/teams/${id}`)
    return response.data.data
  },

  createTeam: async (data: CreateTeamRequest): Promise<Team> => {
    const response = await api.post<ApiResponse<Team>>("/teams", data)
    return response.data.data
  },

  updateTeam: async (id: number, data: UpdateTeamRequest): Promise<Team> => {
    const response = await api.put<ApiResponse<Team>>(`/teams/${id}`, data)
    return response.data.data
  },

  deleteTeam: async (id: number): Promise<void> => {
    await api.delete(`/teams/${id}`)
  },
}
