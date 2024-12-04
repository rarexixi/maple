/**
 * 构建请求url
 * 1. 如果 params 是 number/string/bigint，直接将变量替换为 params 本身
 * 2. 如果 params 是数组，直接将变量替换为 params.join(",")
 * 3. 其他情况，将变量 {paramName} 替换为 params[paramName]
 * 示例：
 * buildUrl('/api/book/{id}', 1) => '/api/book/1'
 * buildUrl('/api/book/{idList}', '1') => '/api/book/1'
 * buildUrl('/api/book/{id}', {id: 1}) => '/api/book/1'
 * buildUrl('/api/book/{idList}', {idList: [1, 2]}) => '/api/book/1,2'
 * buildUrl('/api/book/{id}/{name}', {id: 1, name: 'book'}) => '/api/book/1/book'
 * buildUrl('/api/book/{id}/{name}', {id: 1}) => '/api/book/1/{name}'
 *
 * @param urlTpl url地址模板，变量使用 {paramName} 替代
 * @param params 参数对象，支持对象和数组
 */
function buildUrl(urlTpl: string, params: any) {
  if (!params) {
    return urlTpl
  }
  let paramsType = typeof params
  if (paramsType === 'number' || paramsType === 'string') {
    return urlTpl.replace(/\{([^}]+)}/g, (match, key) => String(params))
  }
  if (Array.isArray(params)) {
    let values = params.join(",")
    return urlTpl.replace(/\{([^}]+)}/g, (match, key) => values)
  }
  return urlTpl.replace(/\{([^}]+)}/g, (match, key) => {
    let placeholders = key.split("|");
    for (let placeholder of placeholders) {
      if (placeholder.endsWith("List") && Array.isArray(params[placeholder])) {
        return params[placeholder].join(",")
      } else if (!!params[placeholder]) {
        return params[placeholder]
      }
    }
    return params[key] || match
  })
}

function getReqUrl(urlTpl: string, method: string) {
  // 返回获取具体请求地址方法
  return (params: any = undefined) => {
    return {
      url: buildUrl(urlTpl, params),
      method: method
    }
  }
}


export const ClusterApis = {
  list: getReqUrl('/api/clusters/all', 'GET'),
  pageList: getReqUrl('/api/clusters', 'GET'),
  detail: getReqUrl('/api/clusters/{name}', 'GET'),
  add: getReqUrl('/api/clusters', 'POST'),
  update: getReqUrl('/api/clusters/{name}', 'PUT'),
  enable: getReqUrl('/api/clusters/enable/{name|nameList}', 'PATCH'),
  disable: getReqUrl('/api/clusters/disable/{name|nameList}', 'PATCH'),
  delete: getReqUrl('/api/clusters/{name|nameList}', 'DELETE'),
}

export const ClusterEngineApis = {
  list: getReqUrl('/api/cluster-engines/all', 'GET'),
  pageList: getReqUrl('/api/cluster-engines', 'GET'),
  detail: getReqUrl('/api/cluster-engines/{id}', 'GET'),
  add: getReqUrl('/api/cluster-engines', 'POST'),
  update: getReqUrl('/api/cluster-engines/{id}', 'PUT'),
  enable: getReqUrl('/api/cluster-engines/enable/{id|idList}', 'PATCH'),
  disable: getReqUrl('/api/cluster-engines/disable/{id|idList}', 'PATCH'),
  delete: getReqUrl('/api/cluster-engines/{id|idList}', 'DELETE'),
}


export const DatasourceTypeApis = {
  list: getReqUrl('/api/datasource-types/all', 'GET'),
  pageList: getReqUrl('/api/datasource-types', 'GET'),
  detail: getReqUrl('/api/datasource-types/{typeCode}', 'GET'),
  add: getReqUrl('/api/datasource-types', 'POST'),
  update: getReqUrl('/api/datasource-types/{typeCode}', 'PUT'),
  enable: getReqUrl('/api/datasource-types/enable/{typeCode|typeCodeList}', 'PATCH'),
  disable: getReqUrl('/api/datasource-types/disable/{typeCode|typeCodeList}', 'PATCH'),
  delete: getReqUrl('/api/datasource-types/{typeCode|typeCodeList}', 'DELETE'),
}


export const DatasourceApis = {
  list: getReqUrl('/api/datasources/all', 'GET'),
  pageList: getReqUrl('/api/datasources', 'GET'),
  detail: getReqUrl('/api/datasources/{id}', 'GET'),
  add: getReqUrl('/api/datasources', 'POST'),
  update: getReqUrl('/api/datasources/{id}', 'PUT'),
  enable: getReqUrl('/api/datasources/enable/{id|idList}', 'PATCH'),
  disable: getReqUrl('/api/datasources/disable/{id|idList}', 'PATCH'),
  delete: getReqUrl('/api/datasources/{id|idList}', 'DELETE'),
}

