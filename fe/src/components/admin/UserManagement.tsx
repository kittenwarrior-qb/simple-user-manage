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
import { userService, type User, type CreateUserRequest } from "@/services/user.service"
import { teamService, type Team } from "@/services/team.service"
import { ArrowUpDown, ChevronDown } from "lucide-react"

export default function UserManagement() {
  const [users, setUsers] = useState<User[]>([])
  const [teams, setTeams] = useState<Team[]>([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [searchKeyword, setSearchKeyword] = useState("")
  const [debouncedSearch, setDebouncedSearch] = useState("")
  const [statusFilter, setStatusFilter] = useState<string>("")
  const [sortBy, setSortBy] = useState("id")
  const [direction, setDirection] = useState<"asc" | "desc">("desc")
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)
  const [createError, setCreateError] = useState<string>("")
  const [formData, setFormData] = useState<CreateUserRequest>({
    userName: "",
    userEmail: "",
    password: "",
    phoneNumber: "",
    userAddress: "",
    role: "USER",
    status: "ACTIVE",
  })
  const isFetchingRef = useRef(false)

  // Debounce search
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchKeyword)
      setPage(0)
    }, 500)
    
    return () => clearTimeout(timer)
  }, [searchKeyword])

  const fetchUsers = useCallback(async () => {
    if (isFetchingRef.current) return
    
    isFetchingRef.current = true
    setLoading(true)
    try {
      const data = await userService.getUsers({
        page,
        size: pageSize,
        sortBy,
        direction,
        userName: debouncedSearch || undefined,
        status: statusFilter || undefined,
      })
      setUsers(data.content)
      setTotalPages(data.totalPages)
      setTotalElements(data.totalElements)
    } catch (error) {
      console.error("Failed to fetch users:", error)
    } finally {
      setLoading(false)
      isFetchingRef.current = false
    }
  }, [page, pageSize, sortBy, direction, debouncedSearch, statusFilter])

  useEffect(() => {
    fetchUsers()
  }, [fetchUsers])

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        const data = await teamService.getAllTeams()
        setTeams(data)
      } catch (error) {
        console.error("Failed to fetch teams:", error)
      }
    }
    fetchTeams()
  }, [])

  const handleStatusChange = async (userId: number, newStatus: "ACTIVE" | "INACTIVE") => {
    console.log("Changing status:", userId, newStatus)
    try {
      if (newStatus === "ACTIVE") {
        await userService.activateUser(userId)
      } else {
        await userService.restrictUser(userId)
      }
      fetchUsers()
    } catch (error) {
      console.error("Failed to change status:", error)
    }
  }

  const handleAssignTeam = async (userId: number, teamId: number) => {
    console.log("Assigning team:", userId, teamId)
    try {
      await userService.assignTeam(userId, teamId)
      fetchUsers()
    } catch (error) {
      console.error("Failed to assign team:", error)
    }
  }

  const handleCreateUser = async () => {
    setCreateError("")
    try {
      await userService.createUser(formData)
      setIsCreateModalOpen(false)
      setFormData({
        userName: "",
        userEmail: "",
        password: "",
        phoneNumber: "",
        userAddress: "",
        role: "USER",
        status: "ACTIVE",
      })
      fetchUsers()
    } catch (error: unknown) {
      console.error("Failed to create user:", error)
      let errorMessage = "Không thể tạo người dùng. Vui lòng kiểm tra lại thông tin."
      if (error && typeof error === 'object' && 'response' in error) {
        const axiosError = error as { response?: { data?: { message?: string } } }
        errorMessage = axiosError.response?.data?.message || errorMessage
      }
      setCreateError(errorMessage)
    }
  }

  const columns: ColumnDef<User, string>[] = [
    {
      accessorKey: "id",
      header: () => <TableColumnHeader title="ID" />,
      cell: ({ row }) => <div className="font-medium">{row.getValue("id")}</div>,
    },
    {
      accessorKey: "userName",
      header: () => <TableColumnHeader title="Tên người dùng" />,
      cell: ({ row }) => <div className="font-medium">{row.getValue("userName")}</div>,
    },
    {
      accessorKey: "userEmail",
      header: () => <TableColumnHeader title="Email" />,
      cell: ({ row }) => <div className="text-muted-foreground">{row.getValue("userEmail")}</div>,
    },
    {
      accessorKey: "phoneNumber",
      header: () => <TableColumnHeader title="Số điện thoại" />,
      cell: ({ row }) => <div>{row.getValue("phoneNumber")}</div>,
    },
    {
      accessorKey: "userAddress",
      header: () => <TableColumnHeader title="Địa chỉ" />,
      cell: ({ row }) => <div>{row.getValue("userAddress")}</div>,
    },
    {
      accessorKey: "role",
      header: () => <TableColumnHeader title="Vai trò" />,
      cell: ({ row }) => {
        const role = row.getValue("role") as string
        return (
          <div
            className={cn(
              "inline-flex items-center rounded-full px-2 py-1 text-xs font-medium min-w-[100px] justify-center",
              role === "ADMIN" ? "bg-blue-100 text-blue-700" : "bg-purple-100 text-purple-700",
            )}
          >
            {role === "ADMIN" ? "Quản trị viên" : "Người dùng"}
          </div>
        )
      },
    },
    {
      accessorKey: "status",
      header: () => <TableColumnHeader title="Trạng thái" />,
      cell: ({ row }) => {
        const user = row.original
        const status = row.getValue("status") as string
        return (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <button
                className={cn(
                  "inline-flex items-center gap-1 rounded-full px-2 py-1 text-xs font-medium cursor-pointer hover:opacity-80 transition-opacity min-w-[120px] justify-center",
                  status === "ACTIVE" ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-600",
                )}
              >
                <span className="truncate">{status === "ACTIVE" ? "Hoạt động" : "Không hoạt động"}</span>
                <ChevronDown className="h-3 w-3 shrink-0" />
              </button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="start">
              <DropdownMenuItem
                onClick={(e) => {
                  e.preventDefault()
                  console.log("Status menu clicked:", user.id, "ACTIVE")
                  handleStatusChange(user.id, "ACTIVE")
                }}
                disabled={status === "ACTIVE"}
              >
                Hoạt động
              </DropdownMenuItem>
              <DropdownMenuItem
                onClick={(e) => {
                  e.preventDefault()
                  console.log("Status menu clicked:", user.id, "INACTIVE")
                  handleStatusChange(user.id, "INACTIVE")
                }}
                disabled={status === "INACTIVE"}
              >
                Không hoạt động
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        )
      },
    },
    {
      id: "team",
      header: () => <TableColumnHeader title="Nhóm" />,
      cell: ({ row }) => {
        const user = row.original
        return (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <button
                className={cn(
                  "inline-flex items-center gap-1 rounded-full px-2 py-1 text-xs font-medium cursor-pointer hover:opacity-80 transition-opacity min-w-[120px] max-w-[200px] justify-center",
                  user.team ? "bg-indigo-100 text-indigo-700" : "bg-gray-100 text-gray-600",
                )}
              >
                <span className="truncate">{user.team ? user.team.teamName : "Chưa có nhóm"}</span>
                <ChevronDown className="h-3 w-3 shrink-0" />
              </button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="start">
              {teams.map((team) => (
                <DropdownMenuItem
                  key={team.id}
                  onClick={(e) => {
                    e.preventDefault()
                    console.log("Team menu clicked:", user.id, team.id)
                    handleAssignTeam(user.id, team.id)
                  }}
                  disabled={user.team?.id === team.id}
                >
                  {team.teamName}
                </DropdownMenuItem>
              ))}
              {teams.length === 0 && (
                <DropdownMenuItem disabled>Không có nhóm nào</DropdownMenuItem>
              )}
            </DropdownMenuContent>
          </DropdownMenu>
        )
      },
    },
  ]

  if (loading && users.length === 0) {
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
        <h1 className="text-3xl font-bold">Quản lý người dùng</h1>
        <Button onClick={() => setIsCreateModalOpen(true)}>Thêm người dùng</Button>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="flex-1">
          <Input
            placeholder="Tìm kiếm theo tên (tự động sau 0.5s)..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
          />
        </div>
        <select
          className="px-4 py-2 border rounded-md"
          value={statusFilter}
          onChange={(e) => {
            setStatusFilter(e.target.value)
            setPage(0)
          }}
        >
          <option value="">Tất cả trạng thái</option>
          <option value="ACTIVE">Hoạt động</option>
          <option value="INACTIVE">Không hoạt động</option>
        </select>
        <select
          className="px-4 py-2 border rounded-md"
          value={sortBy}
          onChange={(e) => {
            setSortBy(e.target.value)
            setPage(0)
          }}
        >
          <option value="id">Sắp xếp theo ID</option>
          <option value="userName">Sắp xếp theo Tên</option>
          <option value="userEmail">Sắp xếp theo Email</option>
        </select>
        <Button
          variant="outline"
          onClick={() => {
            setDirection(direction === "asc" ? "desc" : "asc")
            setPage(0)
          }}
        >
          <ArrowUpDown className="h-4 w-4 mr-2" />
          {direction === "asc" ? "Tăng dần" : "Giảm dần"}
        </Button>
      </div>

      <div className="border rounded-lg">
        <TableProvider columns={columns} data={users}>
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

      <div className="flex items-center justify-between mt-4">
        <div className="text-sm text-muted-foreground">
          Hiển thị {users.length > 0 ? page * pageSize + 1 : 0} - {Math.min((page + 1) * pageSize, totalElements)} trong tổng số {totalElements} người dùng
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            onClick={() => setPage(p => Math.max(0, p - 1))}
            disabled={page === 0 || loading}
          >
            Trước
          </Button>
          <div className="flex items-center gap-2">
            <span className="text-sm">
              Trang {page + 1} / {totalPages || 1}
            </span>
          </div>
          <Button
            variant="outline"
            onClick={() => setPage(p => p + 1)}
            disabled={page >= totalPages - 1 || loading}
          >
            Sau
          </Button>
        </div>
      </div>

      <Dialog open={isCreateModalOpen} onOpenChange={setIsCreateModalOpen}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Thêm người dùng mới</DialogTitle>
            <DialogDescription>Nhập thông tin người dùng mới vào form bên dưới</DialogDescription>
          </DialogHeader>
          {createError && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
              {createError}
            </div>
          )}
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="userName">Tên người dùng</Label>
              <Input
                id="userName"
                value={formData.userName}
                onChange={(e) => setFormData({ ...formData, userName: e.target.value })}
                placeholder="Nhập tên người dùng"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="userEmail">Email</Label>
              <Input
                id="userEmail"
                type="email"
                value={formData.userEmail}
                onChange={(e) => setFormData({ ...formData, userEmail: e.target.value })}
                placeholder="Nhập email"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="password">Mật khẩu</Label>
              <Input
                id="password"
                type="password"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                placeholder="Nhập mật khẩu"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="phoneNumber">Số điện thoại</Label>
              <Input
                id="phoneNumber"
                value={formData.phoneNumber}
                onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                placeholder="Nhập số điện thoại"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="userAddress">Địa chỉ</Label>
              <Input
                id="userAddress"
                value={formData.userAddress}
                onChange={(e) => setFormData({ ...formData, userAddress: e.target.value })}
                placeholder="Nhập địa chỉ"
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
            <Button onClick={handleCreateUser}>Tạo người dùng</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}