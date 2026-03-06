import React from 'react'
import { Navbar } from '@/components/navbar'
import TeamManagement from '@/components/admin/TeamManagement'

const Team = () => {
  return (
    <div className="min-h-svh items-center justify-center max-w-[1280px] mx-auto">
        <Navbar></Navbar>
      <div className="mt-20">
          <TeamManagement></TeamManagement>
      </div>
    </div>
  )
}

export default Team
