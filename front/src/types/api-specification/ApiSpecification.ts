export type ApiSpecification = {
    apiSpecificationId: number;
    domains: DomainWithApiSummary[]
}

export type ApiSummary = {
    apiId: number,
    method: string,
    name: string,
    endpoint: string,
    statusCode: number
}

export type Domain = {
    domainId: number,
    name: string
}

export type RequestHeader = {
    headerKey: string,
    headerValue: string
}

export type PathVariable = {
    name: string,
    description: string
}

export type QueryParam = {
    name: string,
    description: string
}

export type RequestHeaderRow = {
    id: number,
    headerKey: string,
    headerValue: string
}

export type PathVariableRow = {
    id: number,
    name: string,
    description: string
}

export type QueryParamRow = {
    id: number,
    name: string,
    description: string
}

export type DomainWithApiSummary = {
    domainId: number,
    name: string,
    apis: ApiSummary[],
}

export type DomainList = {
    domains: Domain[]
}

export type Api = {
    apiId: number,
    method: string,
    name: string,
    description: string,
    endpoint: string,
    requestSchema: string,
    responseSchema: string,
    statusCode: number,
    requestHeaders: RequestHeader[],
    queryParams: QueryParam[],
    pathVariables: PathVariable[]
}

export type RequestHeaderTestRow = {
    id: number,
    headerKey: string,
    headerValue: string
}

export type PathVariableTestRow = {
    id: number,
    name: string,
    value: string,
    description: string
}

export type QueryParamTestRow = {
    id: number,
    name: string,
    value: string,
    description: string
}

export type RequestBodyFormDataRow = {
    id: number,
    key: string,
    type: string
}

export type RequestBodyFormData = {
    id: number,
    key: string,
    value: FileList | string | null
}