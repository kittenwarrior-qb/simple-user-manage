import api from "@/lib/api"

export interface Team {
  id: number
  teamName: string
  description?: string
  status: "ACTIVE" | "INACTIVE"
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
}
