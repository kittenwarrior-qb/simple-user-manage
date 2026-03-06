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
import { cn } from "@/lib/utils"
import { userService, type User } from "@/services/user.service"

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
            "inline-flex items-center rounded-full px-2 py-1 text-xs font-medium",
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
      const status = row.getValue("status") as string
      return (
        <div
          className={cn(
            "inline-flex items-center rounded-full px-2 py-1 text-xs font-medium",
            status === "ACTIVE" ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-600",
          )}
        >
          {status === "ACTIVE" ? "Hoạt động" : "Không hoạt động"}
        </div>
      )
    },
  },
]

export default function UserManagement() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [searchKeyword, setSearchKeyword] = useState("")
  const [statusFilter, setStatusFilter] = useState<string>("")
  const isFetchingRef = useRef(false)

  const fetchUsers = useCallback(async () => {
    if (isFetchingRef.current) return
    
    isFetchingRef.current = true
    setLoading(true)
    try {
      const data = await userService.getUsers({
        page,
        size: pageSize,
        userName: searchKeyword || undefined,
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
  }, [page, pageSize, searchKeyword, statusFilter])

  useEffect(() => {
    fetchUsers()
  }, [fetchUsers])

  const handleSearch = () => {
    setPage(0)
    fetchUsers()
  }

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      handleSearch()
    }
  }

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
        <Button>Thêm người dùng</Button>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="flex-1">
          <Input
            placeholder="Tìm kiếm theo tên..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            onKeyDown={handleKeyDown}
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
        <Button onClick={handleSearch}>Tìm kiếm</Button>
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
    </div>
  )
}