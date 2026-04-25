export interface FolkSection {
  icon: string
  title: string
  content: string[]
}

export interface FolkCulture {
  id: number
  title: string
  subtitle: string
  category: string
  image: string
  sections: FolkSection[]
}
