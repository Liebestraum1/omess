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

export type DomainWithApiSummary = {
    domainId: number,
    name: string,
    apis: ApiSummary[],
}

export type DomainList = {
    domains: Domain[]
}


