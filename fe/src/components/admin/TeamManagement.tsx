import { useCallback, useEffect, useRef, useState } from "react"
import type { ColumnDef } from "@/components/table-form"
import {
  TableProvider,
  TableHeader,
  TableHeaderGroup,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  TableColumnHeader,
} from "@/components/table-form"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"
import { cn } from "@/lib/utils"
import { teamService, type Team, type CreateTeamRequest, type UpdateTeamRequest } from "@/services/team.service"
import { MoreHorizontal } from "lucide-react"
import { toast } from "sonner"

export default function TeamManagement() {
  const [teams, setTeams] = useState<Team[]>([])
  const [loading, setLoading] = useState(true)
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)
  const [isEditModalOpen, setIsEditModalOpen] = useState(false)
  const [selectedTeam, setSelectedTeam] = useState<Team | null>(null)
  const [createError, setCreateError] = useState<string>("")
  const [editError, setEditError] = useState<string>("")
  const [formData, setFormData] = useState<CreateTeamRequest>({
    teamName: "",
    description: "",
  })
  const [editFormData, setEditFormData] = useState<UpdateTeamRequest>({
    teamName: "",
    description: "",
  })
  const isFetchingRef = useRef(false)

  const fetchTeams = useCallback(async () => {
    if (isFetchingRef.current) return
    
    isFetchingRef.current = true
    setLoading(true)
    try {
      const data = await teamService.getAllTeams()
      setTeams(data)
    } catch (error) {
      console.error("Failed to fetch teams:", error)
      toast.error("Không thể tải danh sách nhóm")
    } finally {
      setLoading(false)
      isFetchingRef.current = false
    }
  }, [])

  useEffect(() => {
    fetchTeams()
  }, [fetchTeams])

  const handleCreateTeam = async () => {
    setCreateError("")
    try {
      await teamService.createTeam(formData)
      setIsCreateModalOpen(false)
      setFormData({
        teamName: "",
        description: "",
      })
      toast.success("Đã tạo nhóm thành công")
      fetchTeams()
    } catch (error: unknown) {
      console.error("Failed to create team:", error)
      let errorMessage = "Không thể tạo nhóm. Vui lòng kiểm tra lại thông tin."
      if (error && typeof error === 'object' && 'response' in error) {
        const axiosError = error as { response?: { data?: { message?: string } } }
        errorMessage = axiosError.response?.data?.message || errorMessage
      }
      setCreateError(errorMessage)
      toast.error("Tạo nhóm thất bại")
    }
  }

  const handleEditTeam = async () => {
    if (!selectedTeam) return
    setEditError("")
    try {
      await teamService.updateTeam(selectedTeam.id, editFormData)
      setIsEditModalOpen(false)
      setSelectedTeam(null)
      toast.success("Đã cập nhật nhóm thành công")
      fetchTeams()
    } catch (error: unknown) {
      console.error("Failed to update team:", error)
      let errorMessage = "Không thể cập nhật nhóm. Vui lòng kiểm tra lại thông tin."
      if (error && typeof error === 'object' && 'response' in error) {
        const axiosError = error as { response?: { data?: { message?: string } } }
        errorMessage = axiosError.response?.data?.message || errorMessage
      }
      setEditError(errorMessage)
      toast.error("Cập nhật nhóm thất bại")
    }
  }

  const handleDeleteTeam = async (teamId: number) => {
    if (!confirm("Bạn có chắc chắn muốn xóa nhóm này?")) return
    
    try {
      await teamService.deleteTeam(teamId)
      toast.success("Đã xóa nhóm thành công")
      fetchTeams()
    } catch (error) {
      console.error("Failed to delete team:", error)
      toast.error("Không thể xóa nhóm")
    }
  }

  const openEditModal = (team: Team) => {
    setSelectedTeam(team)
    setEditFormData({
      teamName: team.teamName,
      description: team.description || "",
    })
    setEditError("")
    setIsEditModalOpen(true)
  }

  const columns: ColumnDef<Team, string>[] = [
    {
      accessorKey: "id",
      header: () => <TableColumnHeader title="ID" />,
      cell: ({ row }) => <div className="font-medium">{row.getValue("id")}</div>,
    },
    {
      accessorKey: "teamName",
      header: () => <TableColumnHeader title="Tên nhóm" />,
      cell: ({ row }) => <div className="font-medium">{row.getValue("teamName")}</div>,
    },
    {
      accessorKey: "description",
      header: () => <TableColumnHeader title="Mô tả" />,
      cell: ({ row }) => {
        const description = row.getValue("description") as string
        return <div className="text-muted-foreground max-w-md truncate">{description || "—"}</div>
      },
    },
    {
      id: "actions",
      header: () => <TableColumnHeader title="Thao tác" />,
      cell: ({ row }) => {
        const team = row.original
        return (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="h-8 w-8 p-0">
                <MoreHorizontal className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem onClick={() => openEditModal(team)}>
                Chỉnh sửa
              </DropdownMenuItem>
              <DropdownMenuItem
                onClick={() => handleDeleteTeam(team.id)}
                className="text-red-600"
              >
                Xóa
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        )
      },
    },
  ]

  if (loading && teams.length === 0) {
    return (
      <div className="container mx-auto py-10">
        <div className="flex justify-center items-center h-64">
          <div className="text-lg">Đang tải...</div>
        </div>
      </div>
    )
  }

  return (
    <div className="container mx-auto py-10">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Quản lý nhóm</h1>
        <Button onClick={() => setIsCreateModalOpen(true)}>Thêm nhóm</Button>
      </div>

      <div className="border rounded-lg">
        <TableProvider columns={columns} data={teams}>
          <TableHeader>
            {({ headerGroup }) => (
              <TableHeaderGroup headerGroup={headerGroup} key={headerGroup.id}>
                {({ header }) => <TableHead header={header} key={header.id} />}
              </TableHeaderGroup>
            )}
          </TableHeader>
          <TableBody>
            {({ row }) => (
              <TableRow row={row} key={row.id}>
                {({ cell }) => <TableCell cell={cell} key={cell.id} />}
              </TableRow>
            )}
          </TableBody>
        </TableProvider>
      </div>

      <div className="mt-4 text-sm text-muted-foreground">
        Tổng số: {teams.length} nhóm
      </div>

      {/* Create Modal */}
      <Dialog open={isCreateModalOpen} onOpenChange={setIsCreateModalOpen}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Thêm nhóm mới</DialogTitle>
            <DialogDescription>Nhập thông tin nhóm mới vào form bên dưới</DialogDescription>
          </DialogHeader>
          {createError && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
              {createError}
            </div>
          )}
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="teamName">Tên nhóm</Label>
              <Input
                id="teamName"
                value={formData.teamName}
                onChange={(e) => setFormData({ ...formData, teamName: e.target.value })}
                placeholder="Nhập tên nhóm"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="description">Mô tả</Label>
              <textarea
                id="description"
                className="px-3 py-2 border rounded-md min-h-[80px] resize-y"
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                placeholder="Nhập mô tả nhóm"
                rows={3}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => {
              setIsCreateModalOpen(false)
              setCreateError("")
            }}>
              Hủy
            </Button>
            <Button onClick={handleCreateTeam}>Tạo nhóm</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Edit Modal */}
      <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Chỉnh sửa nhóm</DialogTitle>
            <DialogDescription>Cập nhật thông tin nhóm</DialogDescription>
          </DialogHeader>
          {editError && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
              {editError}
            </div>
          )}
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="editTeamName">Tên nhóm</Label>
              <Input
                id="editTeamName"
                value={editFormData.teamName}
                onChange={(e) => setEditFormData({ ...editFormData, teamName: e.target.value })}
                placeholder="Nhập tên nhóm"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="editDescription">Mô tả</Label>
              <textarea
                id="editDescription"
                className="px-3 py-2 border rounded-md min-h-[80px] resize-y"
                value={editFormData.description}
                onChange={(e) => setEditFormData({ ...editFormData, description: e.target.value })}
                placeholder="Nhập mô tả nhóm"
                rows={3}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => {
              setIsEditModalOpen(false)
              setEditError("")
            }}>
              Hủy
            </Button>
            <Button onClick={handleEditTeam}>Cập nhật</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}
