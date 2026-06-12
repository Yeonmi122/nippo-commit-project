export interface ReportStatus {
    date: string
    id?: string
  }
  
  export interface ReportDetail {
    id: string
    date: string
    title: string
    content: string
    sender: string
    to: string
    createdAt: string
  }