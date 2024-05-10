import Box from "@mui/material/Box";
import axios from "axios";
import {
    Alert,
    Button,
    Card,
    CardContent,
    Chip,
    Divider, FormControl, FormControlLabel,
    IconButton,
    Modal, RadioGroup,
    Snackbar,
    Typography,
    Radio,
} from "@mui/material";
import {ChangeEvent, useEffect, useMemo, useState} from "react";
import {
    Api,
    PathVariableTestRow,
    QueryParamTestRow,
    RequestBodyFormData,
    RequestHeaderTestRow
} from "../../../types/api-specification/ApiSpecification.ts";
import methodColors from "../HttpMethodModalColor.tsx"
import TextField from "@mui/material/TextField";
import {
    DataGrid,
    GridColDef,
    GridRowModel
} from "@mui/x-data-grid";
import JsonFormatter from 'react-json-formatter'
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import statusCodeColors from "../StatusCodeColors.tsx";
import "../HttpMethodRowColors.css"
import AddCircleIcon from "@mui/icons-material/AddCircle";
import Ajv from "ajv";
import PlayCircleIcon from "@mui/icons-material/PlayCircle";
import {JSONSchemaFaker} from 'json-schema-faker';
import RequestBodyForFormData from "./RequestBodyForFormData.tsx";

interface ErrorState {
    isError: boolean,
    errorMessage: string
}

function CustomNoRowsOverlay() {
    return null;
}

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '90%',
    height: '80%',
    borderRadius: '5px',
    boxShadow: 24,
    p: 2,
    display: 'flex',
    flexDirection: 'column',
    gap: 2,
    overflow: 'auto',
};

const ajv = new Ajv({allErrors: true});

const ApiTestModal = (
    {api, open, changeOpen}:
        {
            api: Api
            open: boolean,
            changeOpen: (isOpen: boolean) => void
        }
) => {
    const [errorState, setErrorState] = useState<ErrorState>({
        isError: false,
        errorMessage: ''
    })
    const [testFired, setTestFired] = useState<boolean>(false)
    const [inputHost, setInputHost] = useState<string>('')
    const [inputApiEndpoint, setInputApiEndpoint] = useState<string>('')
    const [inputApiRequestSchema, setInputApiRequestSchema] = useState<string>('')
    const [inputApiRequestHeaders, setInputApiRequestHeaders] = useState<RequestHeaderTestRow[]>([])
    const [inputApiPathVariables, setInputApiPathVariables] = useState<PathVariableTestRow[]>([])
    const [inputApiQueryParams, setInputApiQueryParams] = useState<QueryParamTestRow[]>([])
    const [inputRequestBodyJson, setInputRequestBodyJson] = useState<string>('')
    const [inputRequestBodyFormData, setInputRequestBodyFormData] = useState<RequestBodyFormData[]>([])
    const [isValidRequestJsonSchema, setIsValidRequestJsonSchema] = useState<boolean>(true);
    const [isValidResponseJsonSchema, setIsValidResponseJsonSchema] = useState<boolean>(true);
    const [requestBodyJsonErrorMessage, setRequestBodyJsonErrorMessage] = useState<string>('');
    const [selectedRadio, setSelectedRadioValue] = useState('none');
    const [responseStatusCode, setResponseStatusCode] = useState<number>(0)
    const [responseBody, setResponseBody] = useState<object>({})
    const [responseBodyJsonErrorMessage, setResponseBodyJsonErrorMessage] = useState<string[]>([]);
    const [isValidStatusCode, setIsValidStatusCode] = useState<boolean>(true);
    const [testAlert, setTestAlert] = useState<boolean>(false)
    const handleClose = () => {
        setInputRequestBodyFormData([])
        setSelectedRadioValue('none')
        setInputApiEndpoint('')
        setIsValidStatusCode(true);
        setIsValidResponseJsonSchema(true)
        setTestFired(false)
        setTestAlert(false)
        changeOpen(false)
    }

    const handleRadioChange = (event: ChangeEvent<HTMLInputElement>) => {
        setSelectedRadioValue(event.target.value);
    };

    const requestHeadersColumns: GridColDef<RequestHeaderTestRow>[] = [
        {
            field: 'remove',
            align: 'center',
            headerName: '',
            resizable: false,
            flex: 1,
            renderCell: (params) => (
                <IconButton
                    // variant="outlined"
                    size="medium"
                    sx={{
                        color: 'red'
                    }}
                    onClick={() => deleteInputApiRequestHeadersRow(params.row.id)}
                >
                    <RemoveCircleIcon/>
                </IconButton>
            ),
        },
        {
            field: 'headerKey',
            headerName: 'Key',
            type: 'string',
            flex: 5,
            editable: true,
        },
        {
            field: 'headerValue',
            headerName: 'Value',
            type: 'string',
            flex: 5,
            editable: true
        },

    ];

    const queryParamsColumns: GridColDef<QueryParamTestRow>[] = [
        {
            field: 'name',
            headerName: '이름',
            type: 'string',
            flex: 1,
            editable: false
        },
        {
            field: 'value',
            headerName: '값',
            type: 'string',
            flex: 2,
            editable: true
        },
        {
            field: 'description',
            headerName: '설명',
            type: 'string',
            flex: 2,
            editable: false
        },
    ];

    const pathVariablesColumns: GridColDef<PathVariableTestRow>[] = [
        {
            field: 'name',
            headerName: '이름',
            type: 'string',
            flex: 1,
            editable: false
        },
        {
            field: 'value',
            headerName: '값',
            type: 'string',
            flex: 2,
            editable: true
        },
        {
            field: 'description',
            headerName: '설명',
            type: 'string',
            flex: 2,
            editable: false
        },
    ];
    const jsonStyle = {
        propertyStyle: {color: 'red'},
        stringStyle: {color: 'green'},
        numberStyle: {color: 'darkorange'}
    }

    const fireTestRequest = async () => {
        if (!isValidRequestJsonSchema || inputHost == '') {
            if (!isValidRequestJsonSchema ) {
                setErrorState({isError: true, errorMessage: "Request Body의 JSON SCHEMA의 유효성을 확인해주세요!"})
            } else {
                setErrorState({isError: true, errorMessage: "Host 주소를 입력해주세요!"})
            }
            return;
        }

        setTestFired(true);
        setTestAlert(true)

        const axiosConfig : {
            method: string,
            url: string,
            withCredentials: boolean,
            headers?: { [header: string] : string}
            data?: FormData | object
        } = {
            method: api.method,
            url: inputHost + inputApiEndpoint,
            withCredentials: true,
        }

        if (inputApiRequestHeaders.length !== 0) {
            axiosConfig.headers = {}; // headers 객체 초기화

            // headers 배열 순회하여 axiosConfig.headers에 추가
            inputApiRequestHeaders.forEach(header => {
                axiosConfig.headers![header.headerKey] = header.headerValue;
            });
        }

        if(selectedRadio === 'application/json'){
            axiosConfig.data = JSON.parse(inputRequestBodyJson)
        }else if(selectedRadio === 'multipart/form-data'){
            const formData = new FormData()
            inputRequestBodyFormData.forEach(formDataRow => {
                console.log(formDataRow.key)
                console.log(formDataRow.value)
                if(formDataRow.value instanceof FileList){
                    for (let i = 0; i < formDataRow.value.length; i++) {
                        formData.append(formDataRow.key, formDataRow.value[i])
                    }
                }else if(typeof formDataRow.value === 'string'){
                    formData.append(formDataRow.key, formDataRow.value)
                }
            })

            axiosConfig.data = formData
        }

        axios(axiosConfig).then(response => {
            setResponseStatusCode(response.status)
            setResponseBody(response.data)
            compareToResponse(response.status, response.data)

        }).catch(error => {
            setResponseStatusCode(error.response.status)
            if (typeof (error.response.data) === 'object') {
                setResponseBody(error.response.data)
            }
            compareToResponse(error.response.status, error.response.data)
        });

    };

    const compareToResponse = (statusCode: number, body: object | string) => {
        if (responseBodySchemaValidator !== undefined) {
            const valid = responseBodySchemaValidator(body);
            if (valid) {
                setIsValidResponseJsonSchema(true)
            } else {
                setIsValidResponseJsonSchema(false)
                let errorCnt = 1
                const errorList: string[] = []
                responseBodySchemaValidator.errors?.forEach((value) => {
                        errorList.push(`${errorCnt++}. Caused By : ` + `[${value.keyword}]` + ' ' + value.dataPath + ' ' + value.message)
                    }
                )
                setResponseBodyJsonErrorMessage(errorList)
            }
        }

        if (statusCode !== api.statusCode) {
            setIsValidStatusCode(false);
        }else{
            setIsValidStatusCode(true)
        }

    }

    const initUpdateInfos = () => {
        setInputApiEndpoint(api.endpoint)
        setInputApiRequestSchema(api.requestSchema)
        setIsValidRequestJsonSchema(true)
        setIsValidResponseJsonSchema(true)

        if (api.requestSchema !== '' && api.requestSchema !== null) {
            setInputRequestBodyJson(JSON.stringify(JSONSchemaFaker.generate(JSON.parse(api.requestSchema)), null, 4))
        }
    }

    const handleChangeHost = (event: ChangeEvent<HTMLInputElement>) => {
        setInputHost(event.target.value)
    }

    const handleChangeEndpoint = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiEndpoint(event.target.value)
    }

    const requestBodySchemaValidator = useMemo(() => {
        if (api.requestSchema !== '' && api.requestSchema !== null) {
            return ajv.compile(JSON.parse(api.requestSchema))
        }
    }, [api])

    const responseBodySchemaValidator = useMemo(() => {
        if (api.responseSchema !== '' && api.responseSchema !== null) {
            return ajv.compile(JSON.parse(api.responseSchema))
        }
    }, [api])

    const handleChangeRequestBodyJson = (event: ChangeEvent<HTMLInputElement>) => {
        setInputRequestBodyJson(event.target.value)
        if (event.target.value !== '' && event.target.value !== null) {
            let parsing;
            try {
                parsing = JSON.parse(event.target.value)
                if (requestBodySchemaValidator !== undefined) {
                    const valid = requestBodySchemaValidator(parsing)
                    if (valid) {
                        setIsValidRequestJsonSchema(true)
                    } else {
                        let errorMessage = ''
                        requestBodySchemaValidator.errors?.forEach((value) =>
                            errorMessage += value.dataPath + ' ' + value.keyword + ' ' + value.message
                        )
                        setRequestBodyJsonErrorMessage(errorMessage)
                        setIsValidRequestJsonSchema(false)
                    }
                }

            } catch (e) {
                setRequestBodyJsonErrorMessage("JSON 형식이 아닙니다.")
                setIsValidRequestJsonSchema(false)
            }
        } else {
            setIsValidRequestJsonSchema(true)
        }
    }

    const handleInputRequestBodyJsonKeyDown = (event: React.KeyboardEvent<HTMLDivElement | HTMLInputElement>) => {
        if (event.key === "Tab") {
            event.preventDefault(); // 브라우저의 기본 동작을 방지합니다.
            const target = event.target as HTMLTextAreaElement; // 입력 필드의 타입을 정확히 지정합니다.
            const start = target.selectionStart;
            const end = target.selectionEnd;

            // 현재 커서 위치에 탭 문자(이 예제에서는 4개의 공백)를 삽입합니다.
            const newValue = inputRequestBodyJson.substring(0, start) + "    " + inputRequestBodyJson.substring(end);

            setInputRequestBodyJson(newValue);

            // 커서 위치를 업데이트합니다.
            setTimeout(() => {
                target.selectionStart = target.selectionEnd = start + 4;
            }, 0);
        }
    }

    const handleRequestHeaderRowUpdate = (newRow: GridRowModel) => {
        // 상태 업데이트 로직
        const updatedRows = inputApiRequestHeaders.map((row) => {
            if (row.id === newRow.id) {
                return {...row, ...newRow};
            }
            return row;
        });
        setInputApiRequestHeaders(updatedRows);
        return newRow;
    };

    const handlePathVariableRowUpdate = (newRow: GridRowModel) => {
        // 상태 업데이트 로직
        const updatedRows = inputApiPathVariables.map((row) => {
            if (row.id === newRow.id) {
                return {...row, ...newRow};
            }
            return row;
        });

        setInputApiPathVariables(updatedRows);

        let newApiEndpoint = api.endpoint;

        const queryParamStartIndexFromApiEndpoint = api.endpoint.indexOf('?')
        const queryParamStartIndexFromInputApiEndpoint = inputApiEndpoint.indexOf('?')

        if (queryParamStartIndexFromApiEndpoint !== -1) {
            newApiEndpoint = newApiEndpoint.substring(0, queryParamStartIndexFromApiEndpoint)
        }

        updatedRows.forEach(value => {
                if (value.value !== '') {
                    newApiEndpoint = newApiEndpoint.replace(`{${value.name}}`, value.value)
                }
            }
        )

        if (queryParamStartIndexFromInputApiEndpoint !== -1) {
            newApiEndpoint += inputApiEndpoint.substring(queryParamStartIndexFromInputApiEndpoint)
        }

        setInputApiEndpoint(newApiEndpoint);

        return newRow;
    };

    const handleQueryParamRowUpdate = (newRow: GridRowModel) => {
        // 상태 업데이트 로직
        const updatedRows = inputApiQueryParams.map((row) => {
            if (row.id === newRow.id) {
                return {...row, ...newRow};
            }
            return row;
        });

        setInputApiQueryParams(updatedRows);

        let newApiEndpoint = inputApiEndpoint
        const idx = inputApiEndpoint.indexOf('?')

        if (idx == -1) {
            newApiEndpoint += '?'
            updatedRows.forEach(value => {
                    newApiEndpoint += `${value.name}=${value.value}&`
                }
            )
        } else {
            newApiEndpoint = inputApiEndpoint.substring(0, idx + 1)
            updatedRows.forEach(value => {
                    newApiEndpoint += `${value.name}=${value.value}&`
                }
            )
        }

        newApiEndpoint = newApiEndpoint.substring(0, newApiEndpoint.length - 1)

        setInputApiEndpoint(newApiEndpoint);

        return newRow;
    };


    const addRowToInputApiRequestHeaders = () => {
        const newRow = {id: inputApiRequestHeaders.length + 1, headerKey: '', headerValue: ''};
        setInputApiRequestHeaders([...inputApiRequestHeaders, newRow]);
    };


    const initArraysToRow = () => {
        const requestHeaderToTestRow = api.requestHeaders.map((requestHeader, index) => ({
            ...requestHeader,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
        }));
        const queryParamToTestRow = api.queryParams.map((queryParam, index) => ({
            ...queryParam,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
            value: ''
        }));
        const pathVariableToTestRow = api.pathVariables.map((pathVariable, index) => ({
            ...pathVariable,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
            value: ''
        }));

        setInputApiRequestHeaders(requestHeaderToTestRow)
        setInputApiPathVariables(pathVariableToTestRow)
        setInputApiQueryParams(queryParamToTestRow)
    }

    const deleteInputApiRequestHeadersRow = (id: number) => {
        setInputApiRequestHeaders((inputApiRequestHeaders.filter(row => row.id !== id)))
    }

    const requestBodyFormatting = () => {
        setInputRequestBodyJson(JSON.stringify(JSON.parse(inputRequestBodyJson), null, 4))
    }


    useEffect(() => {
        initArraysToRow()
        initUpdateInfos()
    }, [api]);

    return (
        <Box>
            <Modal
                open={open}
                onClose={handleClose}
            >
                <Card variant='outlined' sx={modalStyle}
                      style={{backgroundColor: methodColors[`${api.method}-MODAL`], display: open ? 'block' : 'none'}}>
                    <CardContent
                        sx={{
                            borderWidth: '1px',
                            borderStyle: 'solid',
                            borderColor: methodColors[`${api.method}-BADGE`],
                            borderRadius: '4px'
                        }}
                    >
                        <Box
                            sx={{
                                display: 'flex',
                                alignItems: 'center'
                            }}
                        >
                            <Box
                                sx={{
                                    display: 'inline-flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    width: '10%',
                                    padding: '6px 20px',
                                    fontSize: '0.875rem',
                                    fontWeight: '700',
                                    lineHeight: '1.75',
                                    marginRight: '1%',
                                    textTransform: 'uppercase',
                                    borderRadius: '4px',
                                    backgroundColor: methodColors[`${api.method}-BADGE`],
                                    color: 'white',
                                }}
                            >
                                <Typography variant="h5" component="div">
                                    {api.method}
                                </Typography>
                            </Box>

                            <Box sx={{ml: 1}}>
                                <Typography variant="h5" component="div">
                                    {api.name}
                                </Typography>
                            </Box>
                        </Box>

                        <Divider sx={{my: 2}}/>
                        <Typography variant="body1">
                            {api.description}
                        </Typography>

                        <Box sx={{mt: 2}}>
                            <TextField
                                fullWidth
                                variant="outlined"
                                value={inputHost}
                                label="Host (e.g. http://localhost:8080)"
                                required={true}
                                onChange={handleChangeHost}
                                inputProps={{
                                    maxLength: 2000
                                }}
                                sx={{
                                    backgroundColor: 'white',
                                    '& .MuiOutlinedInput-root': {
                                        '& fieldset': {
                                            border: 'none',
                                        },
                                    },
                                }}
                            />
                        </Box>

                        <Divider sx={{my: 2}}/>

                        <Box sx={{mt: 2}}>
                            <TextField
                                fullWidth
                                variant="outlined"
                                label="Endpoint"
                                value={inputApiEndpoint}
                                onChange={handleChangeEndpoint}
                                inputProps={{
                                    maxLength: 2000
                                }}
                                sx={{
                                    backgroundColor: 'white',
                                    '& .MuiOutlinedInput-root': {
                                        '& fieldset': {
                                            border: 'none',
                                        },
                                    },
                                }}
                            />
                        </Box>

                        <Divider sx={{my: 2}}/>

                        <Box>
                            <Box sx={{mt: 2}}>
                                <Typography variant='h6' component='div'>
                                    Request Headers
                                </Typography>
                                <DataGrid
                                    autoHeight={inputApiRequestHeaders.length !== 0}
                                    rows={inputApiRequestHeaders}
                                    columns={requestHeadersColumns as GridColDef<GridRowModel>[]}
                                    hideFooter={true}
                                    hideFooterPagination={true}
                                    disableColumnMenu={true}
                                    disableColumnSorting={true}
                                    disableRowSelectionOnClick={true}
                                    slots={{noRowsOverlay: CustomNoRowsOverlay}}
                                    sx={{
                                        width: '100%',
                                        "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                                            outline: "none",
                                        },
                                        '& .MuiDataGrid-cell': {
                                            borderRight: '1px solid #ccc',
                                        },
                                    }}
                                    processRowUpdate={handleRequestHeaderRowUpdate}
                                />
                                <Chip
                                    icon={<AddCircleIcon style={{color: methodColors[`${api.method}-BADGE`]}}/>}
                                    label='추가'
                                    onClick={() => addRowToInputApiRequestHeaders()}
                                    sx={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        width: '8%',
                                        backgroundColor: 'transparent',
                                        mt: 1
                                    }}
                                />
                            </Box>

                            <Box sx={{mt: 2}}>
                                <Typography variant='h6' component='div'>
                                    Path Variables
                                </Typography>
                                <DataGrid
                                    autoHeight={inputApiPathVariables.length !== 0}
                                    rows={inputApiPathVariables}
                                    columns={pathVariablesColumns as GridColDef<GridRowModel>[]}
                                    hideFooter={true}
                                    hideFooterPagination={true}
                                    disableColumnMenu={true}
                                    disableColumnSorting={true}
                                    disableRowSelectionOnClick={true}
                                    slots={{noRowsOverlay: CustomNoRowsOverlay}}
                                    sx={{
                                        width: '100%',
                                        "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                                            outline: "none",
                                        },
                                        '& .MuiDataGrid-cell': {
                                            borderRight: '1px solid #ccc',
                                        }
                                    }}
                                    processRowUpdate={handlePathVariableRowUpdate}
                                />
                            </Box>

                            <Box sx={{mt: 2}}>
                                <Typography variant='h6' component='div'>
                                    Query Parameters
                                </Typography>
                                <DataGrid
                                    autoHeight={inputApiQueryParams.length !== 0}
                                    rows={inputApiQueryParams}
                                    columns={queryParamsColumns as GridColDef<GridRowModel>[]}
                                    hideFooter={true}
                                    hideFooterPagination={true}
                                    disableColumnMenu={true}
                                    disableColumnSorting={true}
                                    disableRowSelectionOnClick={true}
                                    slots={{noRowsOverlay: CustomNoRowsOverlay}}
                                    sx={{
                                        width: '100%',
                                        "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                                            outline: "none",
                                        },
                                        '& .MuiDataGrid-cell': {
                                            borderRight: '1px solid #ccc',
                                        }
                                    }}
                                    processRowUpdate={handleQueryParamRowUpdate}
                                />
                            </Box>

                            <Box sx={{mt: 2}}>
                                <Typography variant='h6' component='div'>
                                    Request Body
                                </Typography>
                                <FormControl>
                                    <RadioGroup
                                        row
                                        aria-labelledby="demo-row-radio-buttons-group-label"
                                        name="row-radio-buttons-group"
                                        defaultValue='none'
                                    >
                                        <FormControlLabel value="multipart/form-data"
                                                          control={<Radio onChange={handleRadioChange}/>}
                                                          label="form-data"/>
                                        <FormControlLabel value="application/json"
                                                          control={<Radio onChange={handleRadioChange}/>} label="json"/>
                                        <FormControlLabel value="none" control={<Radio onChange={handleRadioChange}/>}
                                                          label="없음"/>
                                    </RadioGroup>
                                </FormControl>

                                <Box
                                    sx={{
                                        display: selectedRadio === 'application/json' ? 'flex' : 'none',
                                        justifyContent: 'space-between'
                                    }}
                                >
                                    <Box sx={{width: '49%'}}>
                                        <Typography variant='body1' component='div'>
                                            Request Body JSON
                                        </Typography>
                                        <TextField
                                            multiline
                                            variant="outlined"
                                            error={!isValidRequestJsonSchema}
                                            value={inputApiRequestSchema ? inputRequestBodyJson : ''}
                                            helperText={!isValidRequestJsonSchema ? requestBodyJsonErrorMessage : ""}
                                            fullWidth
                                            onChange={handleChangeRequestBodyJson}
                                            onKeyDown={handleInputRequestBodyJsonKeyDown}
                                            sx={{
                                                borderRadius: '4px',
                                                backgroundColor: 'white',
                                                '& .MuiOutlinedInput-root': {
                                                    '& fieldset': {
                                                        border: 'none',
                                                    },
                                                },
                                            }}
                                        />
                                        <Button variant='contained'
                                                style={{backgroundColor: methodColors[`${api.method}-BADGE`]}}
                                                sx={{mt: 1}} disabled={!isValidRequestJsonSchema}
                                                onClick={requestBodyFormatting}>
                                            Formatting
                                        </Button>
                                    </Box>

                                    <Box sx={{width: '49%'}}>
                                        <Typography variant='body1' component='div'>
                                            Request Body JSON SCHEMA
                                        </Typography>
                                        <Box sx={{
                                            backgroundColor: 'white',
                                            borderRadius: '4px',
                                        }}>
                                            <Typography variant='body1' component='div'>
                                                {api.requestSchema ?
                                                    <JsonFormatter json={api.requestSchema} tabWith={4}
                                                                   jsonStyle={jsonStyle}/>
                                                    : "없음"
                                                }
                                            </Typography>
                                        </Box>
                                    </Box>
                                </Box>
                                {selectedRadio === 'multipart/form-data' ?
                                    <RequestBodyForFormData isShow={selectedRadio === 'multipart/form-data'}
                                                            method={api.method}
                                                            inputRequestBodyFormData={inputRequestBodyFormData}
                                                            setInputRequestBodyFormData={setInputRequestBodyFormData}/>
                                    : null}
                            </Box>
                        </Box>
                        <Divider sx={{my: 2}}/>

                    </CardContent>

                    <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 1}}>
                        <Button
                            startIcon={<PlayCircleIcon fontSize='large' style={{color: '#72B545'}}/>}
                            sx={{display: 'flex', color: 'white', fontSize: 'medium', backgroundColor: '#356b0e'}}
                            onClick={fireTestRequest}
                            size='large'
                        >
                            테스트
                        </Button>
                    </Box>

                    <Box sx={{display: testFired ? 'block' : 'none'}}>
                        <Typography variant='h5' component='div'>
                            Test Result
                        </Typography>

                        <Box sx={{
                            mt: 2,
                            backgroundColor: 'white',
                            borderColor: isValidStatusCode && isValidResponseJsonSchema ? 'green' : 'red',
                            borderWidth: '1px',
                            borderStyle: 'solid',
                            borderRadius: '4px',
                            alignItems: 'space-between'
                        }}
                        >
                            <Box
                                sx={{padding: 2}}
                            >
                                <Typography variant='h6' color={isValidStatusCode && isValidResponseJsonSchema ? 'green' : 'red'}>
                                    결과 : {isValidStatusCode && isValidResponseJsonSchema ? 'Success' : 'Fail'}
                                </Typography>
                                <Box sx={{padding: 2}}>
                                    <Typography variant='body1' color={isValidStatusCode ? 'green' : 'red'} >
                                        StatusCode : {isValidStatusCode ? "응답 코드가 일치합니다." : "응답코드가 불일치합니다."}
                                    </Typography>
                                    <Typography variant='body1' sx={{mt: 2}} component='div' color={isValidResponseJsonSchema ? 'green' : 'red'}>
                                        Response Body :
                                        {isValidResponseJsonSchema ?
                                            "정의된 Schema에 부합합니다."  :  "정의된 Schema에 일치하지 않습니다."
                                        }

                                        {
                                            isValidResponseJsonSchema ? null :
                                            responseBodyJsonErrorMessage.map((message, index) => (
                                                <Typography sx={{ml: 4, mt: 2}} variant='body1' key={index} component='div'>{message}</Typography>
                                            ))
                                        }
                                    </Typography>
                                </Box>
                            </Box>
                        </Box>

                        <Box sx={{mt: 2, display: 'flex', justifyContent: 'space-between'}}>
                            <Box sx={{width: "49%"}}>
                                <Typography variant='h6' component='div'>
                                    Response Status Code
                                </Typography>
                                <Box
                                    sx={{
                                        display: 'inline-flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        width: '5%',
                                        padding: '6px 20px',
                                        fontSize: '0.875rem',
                                        fontWeight: '700',
                                        lineHeight: '1.75',
                                        marginRight: '1%',
                                        textTransform: 'uppercase',
                                        borderRadius: '4px',
                                        backgroundColor: statusCodeColors[Math.floor(responseStatusCode / 100)],
                                        color: 'white',
                                    }}
                                >
                                    <Typography variant='body1'>
                                        {responseStatusCode}
                                    </Typography>
                                </Box>

                                <Box>
                                    <Typography variant='h6' component='div'>
                                        Response Body
                                    </Typography>
                                    <Box sx={{
                                        backgroundColor: 'white',
                                        borderRadius: '4px',
                                    }}>
                                        <Typography variant='body1' component='div'>
                                            {responseBody ?
                                                <JsonFormatter json={JSON.parse(JSON.stringify(responseBody))} tabWith={4}
                                                               jsonStyle={jsonStyle}/>
                                                : "없음"
                                            }
                                        </Typography>
                                    </Box>
                                </Box>
                            </Box>
                            <Box sx={{width : '49%'}}>
                                <Typography variant='h6' component='div'>
                                    Expected Status Code
                                </Typography>
                                <Box
                                    sx={{
                                        display: 'inline-flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        width: '5%',
                                        padding: '6px 20px',
                                        fontSize: '0.875rem',
                                        fontWeight: '700',
                                        lineHeight: '1.75',
                                        marginRight: '1%',
                                        textTransform: 'uppercase',
                                        borderRadius: '4px',
                                        backgroundColor: statusCodeColors[Math.floor(api.statusCode / 100)],
                                        color: 'white',
                                    }}
                                >
                                    <Typography variant='body1'>
                                        {api.statusCode}
                                    </Typography>
                                </Box>


                                <Box>
                                    <Typography variant='h6' component='div'>
                                        Response Body JSON SCHEMA
                                    </Typography>
                                    <Box sx={{
                                        backgroundColor: 'white',
                                        borderRadius: '4px',
                                    }}>
                                        <Typography variant='body1' component='div'>
                                            {api.responseSchema ?
                                                <JsonFormatter json={api.responseSchema} tabWith={4}
                                                               jsonStyle={jsonStyle}/>
                                                : "없음"
                                            }
                                        </Typography>
                                    </Box>


                                </Box>
                            </Box>
                        </Box>
                    </Box>
                </Card>
            </Modal>
            <Snackbar
                open={errorState.isError}
                autoHideDuration={2000}
                onClose={() => {
                    setErrorState({isError: false, errorMessage: ''})
                }}
                anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
            >
                <Alert
                    severity="error"
                    onClose={() => {
                        setErrorState({isError: false, errorMessage: ''})
                    }}
                    variant="filled"
                    sx={{width: '100%'}}
                >
                    {errorState.errorMessage}
                </Alert>
            </Snackbar>
            <Snackbar
                open={testAlert}
                autoHideDuration={2000}
                anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
                onClose={() => {
                    setTestAlert(false)
                }}
            >
                <Alert
                    severity="success"
                    variant="filled"
                    sx={{width: '100%'}}
                    onClose={() => {
                        setTestAlert(false)
                    }}
                >
                    "아래에서 테스트 결과를 확인해주세요!"
                </Alert>
            </Snackbar>
        </Box>
    )
}

export default ApiTestModal