import Box from "@mui/material/Box";
import {
    Alert, Button,
    Card,
    CardContent,
    Chip,
    Divider,
    IconButton,
    MenuItem,
    Modal,
    Snackbar,
    Typography,
} from "@mui/material";
import {ChangeEvent, useEffect, useState} from "react";
import {
    PathVariable,
    PathVariableRow,
    QueryParam,
    QueryParamRow,
    RequestHeader,
    RequestHeaderRow
} from "../../../types/api-specification/ApiSpecification.ts";
import methodColors from "../HttpMethodModalColor.tsx"
import TextField from "@mui/material/TextField";
import {
    DataGrid,
    GridColDef,
    GridRowModel
} from "@mui/x-data-grid";
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import SaveAsIcon from '@mui/icons-material/SaveAs';
import BackspaceIcon from '@mui/icons-material/Backspace';
import {createApi} from "../request/ApiSpecificationRequest.ts";
import "../HttpMethodRowColors.css"
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
        value: '선택해주세요'
    },
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

const ApiCreateModal = (
    {projectId, apiSpecificationId, domainId, onChildChange, open, changeOpen}:
        {
            projectId: number,
            apiSpecificationId: number,
            domainId: number,
            onChildChange: () => void,
            open: boolean,
            changeOpen: (isOpen: boolean) => void
        }
) => {
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
    const [requestJsonSchemaErrorMessage, setRequestJsonSchemaErrorMessage] = useState<string>('');
    const [responseJsonSchemaErrorMessage, setResponseJsonSchemaErrorMessage] = useState<string>('');

    const handleClose = () => {
        setInputApiName('')
        setInputApiEndpoint('')
        setInputApiDescription('')
        setInputApiStatusCode(0)
        setInputApiRequestSchema('')
        setInputApiResponseSchema('')
        setInputApiMethod('')
        setInputApiRequestHeaders([])
        setInputApiPathVariables([])
        setInputApiQueryParams([])
        setIsValidRequestJsonSchema(true)
        setIsValidResponseJsonSchema(true)
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
            editable: true,
        },
        {
            field: 'headerValue',
            headerName: 'Value',
            type: 'string',
            flex: 1,
            editable: true
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
            editable: true
        },
        {
            field: 'description',
            headerName: '설명',
            type: 'string',
            flex: 2,
            editable: true
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
            editable: true
        },
        {
            field: 'description',
            headerName: '설명',
            type: 'string',
            flex: 2,
            editable: true
        },
    ];

    const handleCreateApi = () => {
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

            createApi(projectId, apiSpecificationId, domainId, inputApiMethod, inputApiName, inputApiDescription, inputApiEndpoint, inputApiStatusCode,
                inputApiRequestSchema, inputApiResponseSchema, toRequestHeaders, toQueryParams, toPathVariables)
                .then(() => {
                    handleClose()
                    onChildChange()
                })
                .catch((e) => {
                    setErrorState({isError: true, errorMessage: e.response.data.title})
                })
        } else {
            setErrorState({isError: true, errorMessage: "JSON SCHEMA의 유효성을 확인해주세요!"})
        }
    };

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
                    setRequestJsonSchemaErrorMessage('JSON SCHEMA DRAFT-07에 부합하지 않는 형식입니다.')
                    setIsValidRequestJsonSchema(false)
                }
            } catch {
                setRequestJsonSchemaErrorMessage('JSON 형식이 아닙니다.')
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
                    setResponseJsonSchemaErrorMessage('JSON SCHEMA DRAFT-07에 부합하지 않는 형식입니다.')
                    setIsValidResponseJsonSchema(false)
                }
            } catch {
                setResponseJsonSchemaErrorMessage('JSON 형식이 아닙니다.')
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

    const requestJsonSchemaFormatting = () => {
        setInputApiRequestSchema(JSON.stringify(JSON.parse(inputApiRequestSchema), null, 4))
    }

    const responseJsonSchemaFormatting = () => {
        setInputApiResponseSchema(JSON.stringify(JSON.parse(inputApiResponseSchema), null, 4))
    }

    useEffect(() => {
    },);

    return (
        <Box>
            <Modal
                open={open}
                onClose={handleClose}
            >
                <Card variant='outlined' sx={modalStyle}
                      style={{backgroundColor: '#E8DEF8', display: open ? 'block' : 'none'}}>
                    <CardContent
                        sx={{
                            borderWidth: '1px',
                            borderStyle: 'solid',
                            borderColor: '#E8DEF8',
                            borderRadius: '4px'
                        }}
                    >
                        <Box
                            sx={{
                                display: 'flex',
                                alignItems: 'center'
                            }}
                        >
                            <TextField
                                id="outlined-select-currency"
                                select
                                label="HTTP Method"
                                defaultValue="선택해주세요"
                                onChange={handleChangeMethod}
                                fullWidth
                                sx={{width: '15%', backgroundColor: 'white'}}
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
                                <TextField
                                    fullWidth
                                    label="API 이름(최대 20자)"
                                    variant="outlined"
                                    value={inputApiName}
                                    onChange={handleChangeName}
                                    inputProps={{
                                        maxLength: 20
                                    }}
                                    sx={{backgroundColor: 'white'}}
                                />
                            </Box>
                        </Box>

                        <Divider sx={{my: 2}}/>

                        <TextField
                            fullWidth
                            label="API 설명(최대 50자)"
                            variant="outlined"
                            value={inputApiDescription}
                            onChange={handleChangeDescription}
                            inputProps={{
                                maxLength: 50
                            }}
                            sx={{backgroundColor: 'white'}}
                        />

                        <Box sx={{mt: 2}}>
                            <Typography variant='h5' component='div'>
                                Endpoint
                            </Typography>
                            <TextField
                                fullWidth
                                variant="outlined"
                                value={inputApiEndpoint}
                                onChange={handleChangeEndpoint}
                                inputProps={{
                                    maxLength: 2000
                                }}
                                sx={{backgroundColor: 'white'}}
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
                                    autoHeight={inputApiRequestHeaders.length !== 0}
                                    rows={inputApiRequestHeaders}
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
                                        '& .MuiDataGrid-cell': {
                                            borderRight: '1px solid #ccc',
                                        }
                                    }}
                                    processRowUpdate={handleRequestHeaderRowUpdate}
                                />
                                <Chip
                                    icon={<AddCircleIcon style={{color: '#4F378B'}}/>}
                                    label='추가'
                                    onClick={() => addRowToInputApiRequestHeaders()}
                                    sx={{
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
                                        '& .MuiDataGrid-cell': {
                                            borderRight: '1px solid #ccc',
                                        }
                                    }}
                                    processRowUpdate={handlePathVariableRowUpdate}
                                />
                                <Chip
                                    icon={<AddCircleIcon style={{color: '#4F378B'}}/>}
                                    label='추가'
                                    onClick={() => addRowToInputApiPathVariables()}
                                    sx={{
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
                                    autoHeight={inputApiQueryParams.length !== 0}
                                    rows={inputApiQueryParams}
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
                                        '& .MuiDataGrid-cell': {
                                            borderRight: '1px solid #ccc',
                                        }
                                    }}
                                    processRowUpdate={handleQueryParamRowUpdate}
                                />

                                <Chip
                                    icon={<AddCircleIcon style={{color: '#4F378B'}}/>}
                                    label='추가'
                                    onClick={() => addRowToInputApiQueryParams()}
                                    sx={{
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
                                <TextField
                                    multiline
                                    variant="outlined"
                                    error={!isValidRequestJsonSchema}
                                    value={inputApiRequestSchema ? inputApiRequestSchema : ''}
                                    helperText={!isValidRequestJsonSchema ? requestJsonSchemaErrorMessage : ""}
                                    fullWidth
                                    onChange={handleChangeRequestSchema}
                                    onKeyDown={handleInputRequestSchemaKeyDown}
                                    sx={{backgroundColor: 'white'}}
                                />
                                <Button variant='contained' style={{backgroundColor: '#4F378B'}} sx={{mt: 1}}
                                        disabled={!isValidRequestJsonSchema} onClick={requestJsonSchemaFormatting}>
                                    Formatting
                                </Button>
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
                            <TextField
                                fullWidth
                                variant="outlined"
                                type='number'
                                onChange={handleChangeStatusCode}
                                inputProps={{
                                    maxLength: 3
                                }}
                                sx={{backgroundColor: 'white'}}
                            />
                        </Box>

                        <Box sx={{mt: 2}}>
                            <Typography variant='h6' component='div'>
                                Response Body JSON SCHEMA
                            </Typography>
                            <TextField
                                multiline
                                variant="outlined"
                                error={!isValidResponseJsonSchema}
                                helperText={!isValidResponseJsonSchema ? responseJsonSchemaErrorMessage : ""}
                                fullWidth
                                onChange={handleChangeResponseSchema}
                                onKeyDown={handleInputResponseSchemaKeyDown}
                                sx={{backgroundColor: 'white'}}
                            />
                            <Button variant='contained' style={{backgroundColor: '#4F378B'}} sx={{mt: 1}}
                                    disabled={!isValidResponseJsonSchema} onClick={responseJsonSchemaFormatting}>
                                Formatting
                            </Button>
                        </Box>
                    </CardContent>


                    <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 1}}>
                        <Button
                            startIcon={<BackspaceIcon style={{color: 'red'}}/>}
                            sx={{display: 'flex', color: 'red', fontSize: 'medium'}}
                            onClick={handleClose}
                        >
                            취소
                        </Button>
                        <Button
                            startIcon={<SaveAsIcon style={{color: '#4F378B'}}/>}
                            sx={{display: 'flex', color: '#4F378B', fontSize: 'medium'}}
                            onClick={handleCreateApi}
                        >
                            저장
                        </Button>
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
        </Box>
    )
}

export default ApiCreateModal