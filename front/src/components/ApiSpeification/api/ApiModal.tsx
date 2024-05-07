import Box from "@mui/material/Box";
import {
    Alert,
    Button,
    Card,
    CardActions,
    CardContent,
    Chip,
    Dialog,
    DialogActions,
    DialogTitle,
    Divider,
    IconButton,
    MenuItem,
    Modal,
    Snackbar,
    Typography,
} from "@mui/material";
import {ChangeEvent, useEffect, useState} from "react";
import {
    Api,
    PathVariable,
    PathVariableRow,
    QueryParam,
    QueryParamRow,
    RequestHeader,
    RequestHeaderRow
} from "../../../types/api-specification/ApiSpecification.ts";
import methodColors from "./HttpMethodModalColor.tsx"
import TextField from "@mui/material/TextField";
import {
    DataGrid,
    GridColDef,
    GridRowModel
} from "@mui/x-data-grid";
import JsonFormatter from 'react-json-formatter'
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import SaveAsIcon from '@mui/icons-material/SaveAs';
import BackspaceIcon from '@mui/icons-material/Backspace';
import {deleteApi, updateApi} from "../request/ApiSpecificationRequest.ts";
import statusCodeColors from "./StatusCodeColors.tsx";
import "./HttpMethodRowColors.css"
import AddCircleIcon from "@mui/icons-material/AddCircle";
import Ajv from "ajv";
import draft7MetaSchema from "ajv/lib/refs/json-schema-draft-07.json";


interface ErrorState {
    isError: boolean,
    errorMessage: string
}

function CustomNoRowsOverlay() {
    return null;
}

const currencies = [
    {
        value: 'GET',
        label: 'GET',
    },
    {
        value: 'POST',
        label: 'POST',
    },
    {
        value: 'HEAD',
        label: 'HEAD',
    },
    {
        value: 'PUT',
        label: 'PUT',
    },
    {
        value: 'DELETE',
        label: 'DELETE',
    },
    {
        value: 'OPTIONS',
        label: 'OPTIONS',
    },
    {
        value: 'PATCH',
        label: 'PATCH',
    },
];


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

const ApiModal = (
    {projectId, apiSpecificationId, domainId, api, onChildChange, open, changeOpen}:
        {
            projectId: number,
            apiSpecificationId: number,
            domainId: number,
            api: Api
            onChildChange: () => void,
            open: boolean,
            changeOpen: (isOpen: boolean) => void
        }
) => {
    const [apiInfo, setApiInfo] = useState<Api | null>(null)
    const [requestHeaders, setRequestHeaders] = useState<RequestHeaderRow[]>([])
    const [queryParams, setQueryParams] = useState<QueryParamRow[]>([])
    const [pathVariables, setPathVariables] = useState<PathVariableRow[]>([])
    const [isUpdateMode, setIsUpdateMode] = useState<boolean>(false)
    const [isDeleteMode, setIsDeleteMode] = useState<boolean>(false)
    const [errorState, setErrorState] = useState<ErrorState>({
        isError: false,
        errorMessage: ''
    })

    const [inputApiName, setInputApiName] = useState<string>('')
    const [inputApiDescription, setInputApiDescription] = useState<string>('')
    const [inputApiEndpoint, setInputApiEndpoint] = useState<string>('')
    const [inputApiStatusCode, setInputApiStatusCode] = useState<number>(0)
    const [inputApiRequestSchema, setInputApiRequestSchema] = useState<string>('')
    const [inputApiResponseSchema, setInputApiResponseSchema] = useState<string>('')
    const [inputApiMethod, setInputApiMethod] = useState<string>('')
    const [inputApiRequestHeaders, setInputApiRequestHeaders] = useState<RequestHeaderRow[]>([])
    const [inputApiPathVariables, setInputApiPathVariables] = useState<PathVariableRow[]>([])
    const [inputApiQueryParams, setInputApiQueryParams] = useState<QueryParamRow[]>([])

    const [isValidRequestJsonSchema, setIsValidRequestJsonSchema] = useState<boolean>(true);
    const [isValidResponseJsonSchema, setIsValidResponseJsonSchema] = useState<boolean>(true);

    const handleClose = () => {
        setIsDeleteMode(false)
        setIsUpdateMode(false)
        setApiInfo(null)
        changeOpen(false)
    }

    const requestHeadersColumns: GridColDef<RequestHeaderRow>[] = [
        {
            field: 'remove',
            align: 'center',
            headerName: '',
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
            flex: 1,
            editable: isUpdateMode,
        },
        {
            field: 'headerValue',
            headerName: 'Value',
            type: 'string',
            flex: 1,
            editable: isUpdateMode
        },

    ];

    const queryParamsColumns: GridColDef<QueryParamRow>[] = [
        {
            field: 'remove',
            align: 'center',
            headerName: '',
            renderCell: (params) => (
                <IconButton
                    // variant="outlined"
                    size="medium"
                    sx={{
                        color: 'red'
                    }}
                    onClick={() => deleteInputApiQueryParamsRow(params.row.id)}
                >
                    <RemoveCircleIcon/>
                </IconButton>
            ),
        },
        {
            field: 'name',
            headerName: '이름',
            type: 'string',
            flex: 1,
            editable: isUpdateMode
        },
        {
            field: 'description',
            headerName: '설명',
            type: 'string',
            flex: 2,
            editable: isUpdateMode
        },
    ];

    const pathVariablesColumns: GridColDef<PathVariableRow>[] = [
        {
            field: 'remove',
            align: 'center',
            headerName: '',
            renderCell: (params) => (
                <IconButton
                    // variant="outlined"
                    size="medium"
                    sx={{
                        color: 'red'
                    }}
                    onClick={() => deleteInputApiPathVariablesRow(params.row.id)}
                >
                    <RemoveCircleIcon/>
                </IconButton>
            ),
        },
        {
            field: 'name',
            headerName: '이름',
            type: 'string',
            flex: 1,
            editable: isUpdateMode
        },
        {
            field: 'description',
            headerName: '설명',
            type: 'string',
            flex: 2,
            editable: isUpdateMode
        },
    ];
    const jsonStyle = {
        propertyStyle: {color: 'red'},
        stringStyle: {color: 'green'},
        numberStyle: {color: 'darkorange'}
    }

    const handleUpdateApi = () => {
        if (isValidRequestJsonSchema && isValidResponseJsonSchema) {
            const toRequestHeaders: RequestHeader[] = inputApiRequestHeaders.map(({headerKey, headerValue}) => ({
                headerKey,
                headerValue
            }));

            const toPathVariables: PathVariable[] = inputApiPathVariables.map(({name, description}) => ({
                name,
                description
            }));

            const toQueryParams: QueryParam[] = inputApiQueryParams.map(({name, description}) => ({
                name,
                description
            }));

            updateApi(projectId, apiSpecificationId, domainId, api.apiId, inputApiMethod, inputApiName, inputApiDescription, inputApiEndpoint, inputApiStatusCode,
                inputApiRequestSchema, inputApiResponseSchema, toRequestHeaders, toPathVariables, toQueryParams)
                .then(() => {
                    setIsUpdateMode(false)
                    handleClose()
                    onChildChange()
                })
                .catch((e) => {
                    setErrorState({isError: true, errorMessage: e.response.data.title})
                })
        }else{
            setErrorState({isError: true, errorMessage: "JSON SCHEMA의 유효성을 확인해주세요!"})
        }
    };

    const handleDeleteApi = () => {
        deleteApi(projectId, apiSpecificationId, domainId, api.apiId)
            .then(() => {
                setIsDeleteMode(false)
                handleClose()
                onChildChange()
            })
            .catch((e) => {
                    setErrorState({isError: true, errorMessage: e.response.data.title})
                }
            )
    }

    const initUpdateInfos = () => {
        setInputApiName(api.name)
        setInputApiMethod(api.method)
        setInputApiDescription(api.description)
        setInputApiEndpoint(api.endpoint)
        setInputApiRequestSchema(api.requestSchema)
        setInputApiResponseSchema(api.responseSchema)
        setInputApiStatusCode(api.statusCode)
        setIsValidRequestJsonSchema(true)
        setIsValidResponseJsonSchema(true)
    }

    const handleChangeMethod = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiMethod(event.target.value)
    }

    const handleChangeName = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiName(event.target.value)
    }

    const handleChangeDescription = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiDescription(event.target.value)
    }

    const handleChangeEndpoint = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiEndpoint(event.target.value)
    }

    const handleChangeStatusCode = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiStatusCode(parseInt(event.target.value))
    }

    const handleChangeRequestSchema = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiRequestSchema(event.target.value)

        if (event.target.value !== '' && event.target.value !== null) {
            let parsing;
            try {
                parsing = JSON.parse(event.target.value)
                const valid = ajv.validate(draft7MetaSchema, parsing)
                if (valid) {
                    setIsValidRequestJsonSchema(true)
                } else {
                    setIsValidRequestJsonSchema(false)
                }
            } catch {
                setIsValidRequestJsonSchema(false)
            }
        } else {
            setIsValidRequestJsonSchema(true)
        }
    }

    const handleChangeResponseSchema = (event: ChangeEvent<HTMLInputElement>) => {
        setInputApiResponseSchema(event.target.value)

        if (event.target.value !== '' && event.target.value !== null) {
            let parsing;
            try {
                parsing = JSON.parse(event.target.value)
                const valid = ajv.validate(draft7MetaSchema, parsing)
                if (valid) {
                    setIsValidResponseJsonSchema(true)
                } else {
                    setIsValidResponseJsonSchema(false)
                }
            } catch {
                setIsValidResponseJsonSchema(false)
            }
        } else {
            setIsValidResponseJsonSchema(true)
        }
    }

    const handleInputRequestSchemaKeyDown = (event: React.KeyboardEvent<HTMLDivElement | HTMLInputElement>) => {
        if (event.key === "Tab") {
            event.preventDefault(); // 브라우저의 기본 동작을 방지합니다.
            const target = event.target as HTMLTextAreaElement; // 입력 필드의 타입을 정확히 지정합니다.
            const start = target.selectionStart;
            const end = target.selectionEnd;

            // 현재 커서 위치에 탭 문자(이 예제에서는 4개의 공백)를 삽입합니다.
            const newValue = inputApiRequestSchema.substring(0, start) + "    " + inputApiRequestSchema.substring(end);

            setInputApiRequestSchema(newValue);

            // 커서 위치를 업데이트합니다.
            setTimeout(() => {
                target.selectionStart = target.selectionEnd = start + 4;
            }, 0);
        }
    }

    const handleInputResponseSchemaKeyDown = (event: React.KeyboardEvent<HTMLDivElement | HTMLInputElement>) => {
        if (event.key === "Tab") {
            event.preventDefault(); // 브라우저의 기본 동작을 방지합니다.
            const target = event.target as HTMLTextAreaElement; // 입력 필드의 타입을 정확히 지정합니다.
            const start = target.selectionStart;
            const end = target.selectionEnd;

            // 현재 커서 위치에 탭 문자(이 예제에서는 4개의 공백)를 삽입합니다.
            const newValue = inputApiResponseSchema.substring(0, start) + "    " + inputApiResponseSchema.substring(end);

            setInputApiResponseSchema(newValue);

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
        return newRow;
    };


    const addRowToInputApiRequestHeaders = () => {
        const newRow = {id: inputApiRequestHeaders.length + 1, headerKey: '', headerValue: ''};
        setInputApiRequestHeaders([...inputApiRequestHeaders, newRow]);
    };

    const addRowToInputApiPathVariables = () => {
        const newRow = {id: inputApiPathVariables.length + 1, name: '', description: ''};
        setInputApiPathVariables([...inputApiPathVariables, newRow]);
    };

    const addRowToInputApiQueryParams = () => {
        const newRow = {id: inputApiQueryParams.length + 1, name: '', description: ''};
        setInputApiQueryParams([...inputApiQueryParams, newRow]);
    };


    const cancelUpdateApi = () => {
        initUpdateInfos()
        initArraysToRow()
        setIsUpdateMode(false)
    }

    const initArraysToRow = () => {
        const requestHeaederToRow = api.requestHeaders.map((requestHeader, index) => ({
            ...requestHeader,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
        }));
        const queryParamToRow = api.queryParams.map((queryParam, index) => ({
            ...queryParam,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
        }));
        const pathVariableToRow = api.pathVariables.map((pathVariable, index) => ({
            ...pathVariable,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
        }));

        setRequestHeaders(requestHeaederToRow);
        setQueryParams(queryParamToRow)
        setPathVariables(pathVariableToRow)
        setInputApiRequestHeaders(requestHeaederToRow)
        setInputApiPathVariables(pathVariableToRow)
        setInputApiQueryParams(queryParamToRow)
    }

    const deleteInputApiRequestHeadersRow = (id: number) => {
        setInputApiRequestHeaders((inputApiRequestHeaders.filter(row => row.id !== id)))
    }

    const deleteInputApiPathVariablesRow = (id: number) => {
        setInputApiPathVariables((inputApiPathVariables.filter(row => row.id !== id)))
    }

    const deleteInputApiQueryParamsRow = (id: number) => {
        setInputApiQueryParams((inputApiQueryParams.filter(row => row.id !== id)))
    }

    const ajv = new Ajv({allErrors: true});

    useEffect(() => {
        setApiInfo(api)
        initArraysToRow()
        initUpdateInfos()
    }, [api]);

    return (
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
                                display: !isUpdateMode ? 'inline-flex' : 'none',
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
                        <TextField
                            id="outlined-select-currency"
                            select
                            label="HTTP Method"
                            defaultValue={api.method}
                            onChange={handleChangeMethod}
                            fullWidth
                            sx={{display: isUpdateMode ? 'block' : 'none', width: '15%', backgroundColor: 'white'}}
                        >
                            {currencies.map((option) => (
                                <MenuItem key={option.value} value={option.value}
                                          sx={{alignItems: 'center', justifyContent: 'center'}}>
                                    <Box
                                        sx={{
                                            display: 'inline-flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            width: '65%',
                                            padding: '6px 20px',
                                            // fontSize: '20px',
                                            // fontWeight: '500',
                                            // lineHeight: '1.75',
                                            textTransform: 'uppercase',
                                            borderRadius: '4px',
                                            backgroundColor: methodColors[`${option.label}-BADGE`],
                                            color: 'white',
                                        }}
                                    >
                                        <Typography variant="h6" component="div">
                                            {option.label}
                                        </Typography>
                                    </Box>
                                </MenuItem>
                            ))}
                        </TextField>


                        <Box sx={{ml: 1}}>
                            <Typography variant="h5" component="div"
                                        sx={{display: !isUpdateMode ? 'block' : 'none'}}>
                                {api.name}
                            </Typography>
                            <TextField
                                fullWidth
                                label="API 이름(최대 20자)"
                                variant="outlined"
                                value={inputApiName}
                                onChange={handleChangeName}
                                inputProps={{
                                    maxLength: 20
                                }}
                                sx={{display: isUpdateMode ? 'block' : 'none', backgroundColor: 'white'}}
                            />
                        </Box>

                        <CardActions>
                            <IconButton
                                aria-label="modifiy"
                                color='warning'
                                sx={{padding: 0, display: !isUpdateMode && !isDeleteMode ? 'block' : 'none'}}
                                onClick={() => {
                                    setIsUpdateMode(true)
                                }}
                            >
                                <EditIcon/>
                            </IconButton>
                            <IconButton
                                aria-label="delete"
                                color='error'
                                sx={{padding: 0, display: !isUpdateMode && !isDeleteMode ? 'block' : 'none'}}
                                onClick={() => setIsDeleteMode(true)}
                            >
                                <DeleteIcon/>
                            </IconButton>

                            <Dialog
                                open={isDeleteMode}
                                onClose={() => setIsDeleteMode(false)}
                                aria-labelledby="alert-dialog-title"
                                aria-describedby="alert-dialog-description"
                            >
                                <DialogTitle id="alert-dialog-title">
                                    {"API를 삭제하시겠습니까?"}
                                </DialogTitle>
                                <DialogActions>
                                    <Button color='secondary' onClick={() => setIsDeleteMode(false)}>취소</Button>
                                    <Button color='error' onClick={() => handleDeleteApi()} autoFocus>
                                        삭제
                                    </Button>
                                </DialogActions>
                            </Dialog>
                        </CardActions>
                    </Box>

                    <Divider sx={{my: 2}}/>
                    <Typography variant="body1" sx={{display: !isUpdateMode ? 'block' : 'none'}}>
                        {api.description}
                    </Typography>

                    <TextField
                        fullWidth
                        label="API 설명(최대 50자)"
                        variant="outlined"
                        value={inputApiDescription}
                        onChange={handleChangeDescription}
                        inputProps={{
                            maxLength: 50
                        }}
                        sx={{display: isUpdateMode ? 'block' : 'none', backgroundColor: 'white'}}
                    />

                    <Box sx={{mt: 2}}>
                        <Typography variant='h5' component='div'>
                            Endpoint
                        </Typography>
                        <Box sx={{
                            backgroundColor: 'white',
                            borderRadius: '4px',
                            display: !isUpdateMode ? 'block' : 'none'
                        }}>

                            <Typography variant='body1' mt={1}>
                                {api.endpoint}
                            </Typography>
                        </Box>

                        <TextField
                            fullWidth
                            variant="outlined"
                            value={inputApiEndpoint}
                            onChange={handleChangeEndpoint}
                            inputProps={{
                                maxLength: 2000
                            }}
                            sx={{display: isUpdateMode ? 'block' : 'none', backgroundColor: 'white'}}
                        />
                    </Box>

                    <Divider sx={{my: 2}}/>

                    <Box>
                        <Typography variant='h5' component='div'>
                            Request
                        </Typography>
                        <Box sx={{mt: 2}}>
                            <Typography variant='h6' component='div'>
                                Request Headers
                            </Typography>
                            <DataGrid
                                autoHeight={requestHeaders.length !== 0 && inputApiRequestHeaders.length !== 0}
                                rows={!isUpdateMode ? requestHeaders : inputApiRequestHeaders}
                                columns={requestHeadersColumns as GridColDef<GridRowModel>[]}
                                hideFooter={true}
                                hideFooterPagination={true}
                                disableRowSelectionOnClick
                                disableColumnMenu={true}
                                disableColumnResize={true}
                                disableColumnSelector={true}
                                disableColumnSorting={true}
                                slots={{noRowsOverlay: CustomNoRowsOverlay}}
                                sx={{
                                    width: '100%',
                                    "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                                        outline: "none",
                                    },
                                }}
                                columnVisibilityModel={{
                                    remove: isUpdateMode
                                }}
                                getRowClassName={() => isUpdateMode ? 'EDIT-ROW' : ''}
                                processRowUpdate={handleRequestHeaderRowUpdate}
                            />
                            <Chip
                                icon={<AddCircleIcon style={{color: methodColors[`${api.method}-BADGE`]}}/>}
                                label='추가'
                                onClick={() => addRowToInputApiRequestHeaders()}
                                sx={{
                                    display: isUpdateMode ? 'flex' : 'none',
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
                                autoHeight={pathVariables.length !== 0 && inputApiPathVariables.length !== 0}
                                rows={!isUpdateMode ? pathVariables : inputApiPathVariables}
                                columns={pathVariablesColumns as GridColDef<GridRowModel>[]}
                                hideFooter={true}
                                hideFooterPagination={true}
                                disableRowSelectionOnClick
                                disableColumnMenu={true}
                                disableColumnResize={true}
                                disableColumnSelector={true}
                                disableColumnSorting={true}
                                slots={{noRowsOverlay: CustomNoRowsOverlay}}
                                sx={{
                                    width: '100%',
                                    "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                                        outline: "none",
                                    },
                                }}
                                columnVisibilityModel={{
                                    remove: isUpdateMode
                                }}
                                getRowClassName={() => isUpdateMode ? 'EDIT-ROW' : ''}
                                processRowUpdate={handlePathVariableRowUpdate}
                            />
                            <Chip
                                icon={<AddCircleIcon style={{color: methodColors[`${api.method}-BADGE`]}}/>}
                                label='추가'
                                onClick={() => addRowToInputApiPathVariables()}
                                sx={{
                                    display: isUpdateMode ? 'flex' : 'none',
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
                                Query Parameters
                            </Typography>
                            <DataGrid
                                autoHeight={queryParams.length !== 0 && inputApiQueryParams.length !== 0}
                                rows={!isUpdateMode ? queryParams : inputApiQueryParams}
                                columns={queryParamsColumns as GridColDef<GridRowModel>[]}
                                hideFooter={true}
                                hideFooterPagination={true}
                                disableRowSelectionOnClick
                                disableColumnMenu={true}
                                disableColumnResize={true}
                                disableColumnSelector={true}
                                disableColumnSorting={true}
                                slots={{noRowsOverlay: CustomNoRowsOverlay}}
                                sx={{
                                    width: '100%',
                                    "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                                        outline: "none",
                                    },
                                }}
                                columnVisibilityModel={{
                                    remove: isUpdateMode
                                }}
                                getRowClassName={() => isUpdateMode ? 'EDIT-ROW' : ''}
                                processRowUpdate={handleQueryParamRowUpdate}
                            />

                            <Chip
                                icon={<AddCircleIcon style={{color: methodColors[`${api.method}-BADGE`]}}/>}
                                label='추가'
                                onClick={() => addRowToInputApiQueryParams()}
                                sx={{
                                    display: isUpdateMode ? 'flex' : 'none',
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
                                Request Body JSON SCHEMA
                            </Typography>
                            <Box sx={{
                                backgroundColor: 'white',
                                borderRadius: '4px',
                                display: !isUpdateMode ? 'block' : 'none'
                            }}>
                                <Typography variant='body1' component='div'>
                                    {api.requestSchema ?
                                        <JsonFormatter json={apiInfo?.requestSchema} tabWith={4}
                                                       jsonStyle={jsonStyle}/>
                                        : "없음"
                                    }
                                </Typography>
                            </Box>

                            <TextField
                                multiline
                                variant="outlined"
                                error={!isValidRequestJsonSchema}
                                value={inputApiRequestSchema ? inputApiRequestSchema : ''}
                                helperText={!isValidRequestJsonSchema ? "유효하지 않은 JSON SCHEMA입니다." : ""}
                                fullWidth
                                onChange={handleChangeRequestSchema}
                                onKeyDown={handleInputRequestSchemaKeyDown}
                                sx={{display: isUpdateMode ? 'block' : 'none', backgroundColor: 'white'}}
                            />
                        </Box>
                    </Box>

                    <Divider sx={{my: 2}}/>


                    <Typography variant='h5' component='div'>
                        Response
                    </Typography>

                    <Box sx={{mt: 2}}>
                        <Typography variant='h6' component='div'>
                            Status Code
                        </Typography>
                        <Box
                            sx={{
                                display: !isUpdateMode ? 'inline-flex' : 'none',
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
                        <TextField
                            fullWidth
                            variant="outlined"
                            type='number'
                            value={inputApiStatusCode}
                            onChange={handleChangeStatusCode}
                            inputProps={{
                                maxLength: 3
                            }}
                            sx={{display: isUpdateMode ? 'block' : 'none', backgroundColor: 'white'}}
                        />
                    </Box>

                    <Box sx={{mt: 2}}>
                        <Typography variant='h6' component='div'>
                            Response Body JSON SCHEMA
                        </Typography>
                        <Box sx={{
                            backgroundColor: 'white',
                            borderRadius: '4px',
                            display: !isUpdateMode ? 'block' : 'none'
                        }}>
                            <Typography variant='body1' component='div'>
                                {api.responseSchema ?
                                    <JsonFormatter json={apiInfo?.responseSchema} tabWith={4}
                                                   jsonStyle={jsonStyle}/>
                                    : "없음"
                                }
                            </Typography>

                        </Box>
                        <TextField
                            multiline
                            variant="outlined"
                            error={!isValidResponseJsonSchema}
                            value={inputApiResponseSchema ? inputApiResponseSchema : ''}
                            helperText={!isValidResponseJsonSchema ? "유효하지 않은 JSON SCHEMA입니다." : ""}
                            fullWidth
                            onChange={handleChangeResponseSchema}
                            onKeyDown={handleInputResponseSchemaKeyDown}
                            sx={{display: isUpdateMode ? 'block' : 'none', backgroundColor: 'white'}}
                        />
                    </Box>
                </CardContent>

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
                <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 1}}>
                    <IconButton
                        sx={{display: isUpdateMode ? 'flex' : 'none', color: 'red', fontSize: 'medium'}}
                        onClick={cancelUpdateApi}
                    >
                        <BackspaceIcon style={{color: 'red'}}/>
                        취소
                    </IconButton>

                    <IconButton
                        sx={{display: isUpdateMode ? 'flex' : 'none', color: '#4F378B', fontSize: 'medium'}}
                        onClick={handleUpdateApi}
                    >
                        <SaveAsIcon style={{color: '#4F378B'}}/>
                        저장
                    </IconButton>

                    {/*<Button*/}
                    {/*    variant="outlined"*/}
                    {/*    onClick={cancelUpdateApi}*/}
                    {/*    color='secondary'*/}
                    {/*    sx={{width: '25%', height: '55px', display: isUpdateMode ? 'block' : 'none'}}*/}
                    {/*>*/}
                    {/*    취소*/}
                    {/*</Button>*/}
                    {/*<Button*/}
                    {/*    variant="contained"*/}
                    {/*    onClick={handleUpdateApi}*/}
                    {/*    sx={{width: '25%', height: '55px', display: isUpdateMode ? 'block' : 'none'}}*/}
                    {/*>*/}
                    {/*    저장*/}
                    {/*</Button>*/}
                </Box>
            </Card>

        </Modal>
    )
}

export default ApiModal