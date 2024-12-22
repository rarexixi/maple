export function getDatasourceOptions(dataList: any[], ...dsType: string[]) {
  return dataList.filter((ds: any) => dsType.includes(ds.datasourceType)).map(item => ({value: item.id, label: `${item.name} (${item.description})`}))
}