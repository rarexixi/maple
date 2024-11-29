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

<#list tableModels as table>
<#include "/include/table/properties.ftl">

export const ${className}Apis = {
  list: getReqUrl('/api/${tablePluralPath}/all', 'GET'),
  pageList: getReqUrl('/api/${tablePluralPath}', 'GET'),
  detail: getReqUrl('/api/${tablePluralPath}<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}}</#list>', 'GET'),
  add: getReqUrl('/api/${tablePluralPath}', 'POST'),
  update: getReqUrl('/api/${tablePluralPath}<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}}</#list>', 'PUT'),
  <#if (table.validStatusColumn??)>
  enable: getReqUrl('/api/${tablePluralPath}/enable<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}<#if (table.hasUniPk)>|${fieldName}List</#if>}</#list>', 'PATCH'),
  disable: getReqUrl('/api/${tablePluralPath}/disable<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}<#if (table.hasUniPk)>|${fieldName}List</#if>}</#list>', 'PATCH'),
  </#if>
  delete: getReqUrl('/api/${tablePluralPath}<#list pks as column><#include "/include/column/properties.ftl">/{${fieldName}<#if (table.hasUniPk)>|${fieldName}List</#if>}</#list>', 'DELETE'),
}

</#list>