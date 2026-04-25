export interface SpotSection {
  icon: string
  title: string
  content: string
}

export interface TourSpot {
  id: string
  name: string
  address: string
  cover: string
  link?: string
  sections: SpotSection[]
}
