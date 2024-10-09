export interface PaginatedResponse<T> {
    totalPages: number
    totalElements: number
    size: number
    content: T[]
    number: number
    sort: Sort[]
    first: boolean
    last: boolean
    numberOfElements: number
    pageable: Pageable
    empty: boolean
}


export interface Sort {
    direction: string
    nullHandling: string
    ascending: boolean
    property: string
    ignoreCase: boolean
}

export interface Pageable {
    offset: number
    paged: boolean
    pageNumber: number
    pageSize: number
    unpaged: boolean
}

