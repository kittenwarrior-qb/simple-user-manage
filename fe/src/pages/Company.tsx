import React from 'react'
import { Navbar } from '@/components/navbar'
import CompanyManagement from '@/components/admin/CompanyManagement'

const Company = () => {
  return (
    <div className="min-h-svh items-center justify-center max-w-[1280px] mx-auto">
        <Navbar></Navbar>
      <div className="mt-20">
          <CompanyManagement></CompanyManagement>
      </div>
    </div>
  )
}

export default Company
