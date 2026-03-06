import UserManagement from '@/components/admin/UserManagement'
import { Navbar } from '@/components/navbar'

const User = () => {
  return (
    <div className="min-h-svh items-center justify-center max-w-[1280px] mx-auto">
        <Navbar></Navbar>
      <div className="mt-20">
          <UserManagement></UserManagement>
      </div>
    </div>
  )
}

export default User