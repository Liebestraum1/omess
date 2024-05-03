import Box from '@mui/material/Box';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import {ApiSummary} from "../../../types/api-specification/ApiSpecification.ts";
import {useEffect, useState} from "react";
import {Button} from "@mui/material";

function CustomNoRowsOverlay() {
    return null;
}
const columns: GridColDef<ApiSummary>[] = [
    {
        field: 'method',
        headerName: 'Method',
        type: 'string',
        width: 100,
        disableColumnMenu: true,
        resizable: false
    },
    {
        field: 'name',
        headerName: '이름',
        type: 'string',
        width: 250,
    },
    {
        field: 'endpoint',
        headerName: 'Endpoint',
        type: 'string',
        width: 400,
    },
    {
        field: 'statusCode',
        headerName: '상태 코드',
        type: 'number',
        width: 150,
        disableColumnMenu: true,
        resizable: false
    },
    {
        field: 'detail',
        headerName: '',
        renderCell: () => (
                <Button
                    variant="contained"
                    size="small"
                    style={{ marginLeft: 16 }}
                >
                    열기
                </Button>

        ),
    },
];

const ApiListComponent = ({apis}: { apis: ApiSummary[], }) => {
    // 컴포넌트 내부에서 rows 상태를 관리
    const [rows, setRows] = useState<ApiSummary[]>([]);

    // apis prop이 변경될 때마다 rows 상태를 업데이트함
    useEffect(() => {

        const rowsWithId = apis.map((api, index) => ({
            ...api,
            // index를 id로 사용. 필요하다면 index에 1을 더하는 등의 조정도 가능.
            id: index,
        }));

        setRows(rowsWithId);
    }, [apis]);

    return (
        <>
            <Box sx={{width: '100%' }}>
                <DataGrid
                    className={""}
                    rows={rows}
                    columns={columns}
                    disableRowSelectionOnClick
                    hideFooter={true}
                    hideFooterPagination={true}
                    slots={{noRowsOverlay: CustomNoRowsOverlay}}
                    sx={{
                        width: '100%',
                        "& .MuiDataGrid-cell:focus, & .MuiDataGrid-cell:focus-within": {
                            outline: "none",
                        },
                    }}

                />
            </Box>
        </>
    )
}

export default ApiListComponent;
