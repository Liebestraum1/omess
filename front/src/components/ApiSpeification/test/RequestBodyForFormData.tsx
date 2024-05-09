import {ChangeEvent, useState} from "react";
import {RequestBodyFormData, RequestBodyFormDataRow} from "../../../types/api-specification/ApiSpecification.ts";
import {DataGrid, GridColDef, GridRenderCellParams, GridRowModel} from "@mui/x-data-grid";
import {Chip, IconButton} from "@mui/material";
import RemoveCircleIcon from "@mui/icons-material/RemoveCircle";
import methodColors from "../HttpMethodModalColor.tsx";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import TextFieldsIcon from '@mui/icons-material/TextFields';
import AttachFileIcon from '@mui/icons-material/AttachFile';

function CustomNoRowsOverlay() {
    return null;
}

const RequestBodyForFormData = (
    {isShow, method, inputRequestBodyFormData, setInputRequestBodyFormData}:
        {
            isShow: boolean,
            method: string,
            inputRequestBodyFormData: RequestBodyFormData[],
            setInputRequestBodyFormData: (requestBodyFormData: RequestBodyFormData[]) => void
        }
) => {

    const [inputFormDataRows, setInputFormDataRows] = useState<RequestBodyFormDataRow[]>([])

    const requestHeadersColumns: GridColDef<RequestBodyFormDataRow>[] = [
        {
            field: 'remove',
            align: 'center',
            headerName: '',
            renderCell: (params: GridRenderCellParams) => (
                <IconButton
                    // variant="outlined"
                    size="medium"
                    sx={{
                        color: 'red'
                    }}
                    onClick={() => deleteInputFormDataRows(params.row.id)}
                >
                    <RemoveCircleIcon/>
                </IconButton>
            ),
        },
        {
            field: 'key',
            headerName: 'Key',
            flex: 5,
            renderCell: (params: GridRenderCellParams) => (
                <TextField
                    fullWidth type="text"
                    onChange={(event: ChangeEvent<HTMLInputElement>) => handleChangeOnKey(params.row.id, event)}
                    sx={{
                        '& .MuiOutlinedInput-root': {
                            '& fieldset': {
                                border: 'none',
                            },
                        },
                    }}
                />
            ),
        },
        {
            field: 'value',
            headerName: 'Value',
            flex: 5,
            renderCell: (params: GridRenderCellParams) => {
                if (params.row.type === "file") {
                    return <TextField
                        fullWidth
                        type="file"
                        inputProps={{multiple: true}}
                        onChange={(event: ChangeEvent<HTMLInputElement>) => handleChangeOnFile(params.row.id, event)}
                        sx={{
                            '& .MuiOutlinedInput-root': {
                                '& fieldset': {
                                    border: 'none',
                                },
                            },
                        }}
                    />;
                } else {
                    return <TextField
                        fullWidth type="text"
                        onChange={(event: ChangeEvent<HTMLInputElement>) => handleChangeOnValue(params.row.id, event)}
                        sx={{
                            '& .MuiOutlinedInput-root': {
                                '& fieldset': {
                                    border: 'none',
                                },
                            },
                        }}
                    />
                }
            },
        },
        {
            field: 'type',
            type: 'string',
        }
    ];

    const addRowToInputFormDataRows = (rowType: string) => {
        const newRow = {id: inputFormDataRows.length + 1, key: '', type: rowType};
        setInputFormDataRows([...inputFormDataRows, newRow]);

        if (rowType === 'text') {
            const newData = {id: inputFormDataRows.length + 1, key: '', value: ''}
            setInputRequestBodyFormData([...inputRequestBodyFormData, newData])
        } else {
            const newData = {id: inputFormDataRows.length + 1, key: '', value: null}
            setInputRequestBodyFormData([...inputRequestBodyFormData, newData])
        }
    };

    const deleteInputFormDataRows = (id: number) => {
        setInputFormDataRows((inputFormDataRows.filter(row => row.id !== id)))
        setInputRequestBodyFormData((inputRequestBodyFormData.filter((data => data.id !== id))))
    }

    const handleFormDataRowUpdate = (newRow: GridRowModel) => {
        // 상태 업데이트 로직
        const updatedRows = inputFormDataRows.map((row) => {
            if (row.id === newRow.id) {
                return {...row, ...newRow};
            }
            return row;
        });
        setInputFormDataRows(updatedRows);
        return newRow;
    };

    const handleChangeOnKey = (rowId: number, event: ChangeEvent<HTMLInputElement>) => {
        const newKey = event.target.value
        const updatedData = inputRequestBodyFormData.map((data) => {
            if (data.id === rowId) {
                return {...data, key: newKey};
            }
            return data;
        });

        setInputRequestBodyFormData(updatedData)
    }

    const handleChangeOnFile = (rowId: number, event: ChangeEvent<HTMLInputElement>) => {
        const newFileArray = event.target.files ? event.target.files : null;
        const updatedData = inputRequestBodyFormData.map((data) => {
            if (data.id === rowId) {
                return {...data, value: newFileArray};
            }
            return data;
        });

        setInputRequestBodyFormData(updatedData)
    }

    const handleChangeOnValue = (rowId: number, event: ChangeEvent<HTMLInputElement>) => {
        const newValue = event.target.value
        const updatedData = inputRequestBodyFormData.map((data) => {
            if (data.id === rowId) {
                return {...data, value: newValue};
            }
            return data;
        });

        setInputRequestBodyFormData(updatedData)
    }

    return (
        <Box sx={{display: isShow ? 'block' : 'none'}}>
            <DataGrid
                autoHeight={inputFormDataRows.length !== 0}
                rows={inputFormDataRows}
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
                    },
                }}
                // getRowClassName={() => 'EDIT-ROW'}
                processRowUpdate={handleFormDataRowUpdate}
                columnVisibilityModel={{
                    type: false
                }}
            />

            <Box sx={{display: 'flex', mt: 2}}>
                <Chip
                    icon={<AttachFileIcon style={{color: methodColors[`${method}-BADGE`]}}/>}
                    label='Add File'
                    onClick={() => addRowToInputFormDataRows('file')}
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        backgroundColor: 'transparent',
                        fontWeight: 'bold',
                    }}
                />
                <Chip
                    icon={<TextFieldsIcon style={{color: methodColors[`${method}-BADGE`]}}/>}
                    label='Add Text'
                    onClick={() => addRowToInputFormDataRows('text')}
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        backgroundColor: 'transparent',
                        fontWeight: 'bold',
                    }}
                />
            </Box>
        </Box>
    )
}

export default RequestBodyForFormData