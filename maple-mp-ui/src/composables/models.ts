export interface PageInfo {
  list: Array<any>
  total: number
  pageNum: number
  pageSize: number
}

export interface SearchPageParams {
  pageNum: number
  pageSize: number
}

export interface RdbmsTable {
  databaseName: string
  schemaName: string
  tableName: string
}

export type validateFunction = (setValidated: (success: boolean) => void) => Promise<void>

export interface ValidatableComponent {
  validate: validateFunction
}
