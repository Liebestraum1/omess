import Box from '@mui/material/Box';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import {Api, ApiSummary} from "../../../types/api-specification/ApiSpecification.ts";
import {useEffect, useState} from "react";
import {Button, IconButton} from "@mui/material";
import PlayCircleIcon from '@mui/icons-material/PlayCircle';
import ApiInfoAndUpdateModal from "./ApiInfoAndUpdateModal.tsx";
import methodColors from "../HttpMethodModalColor.tsx"
import {loadApi} from "../request/ApiSpecificationRequest.ts";
import "../HttpMethodRowColors.css"
import ApiTestModal from "../test/ApiTestModal.tsx";

function CustomNoRowsOverlay() {
    return null;
}

const ApiListComponent = ({projectId, apiSpecificationId, domainId, onChildChange, apis}:
                              {projectId: number, apiSpecificationId: number, domainId: number, onChildChange: () => void, apis: ApiSummary[], }) => {
    // 컴포넌트 내부에서 rows 상태를 관리
    const [rows, setRows] = useState<ApiSummary[]>([]);
    const [api, setApi] = useState<Api>({
        apiId: -1,
        description: "",
        endpoint: "",
        method: "",
        name: "",
        pathVariables: [],
        queryParams: [],
        requestHeaders: [],
        requestSchema: "",
        responseSchema: "",
        statusCode: 0
    })
    const [isOpenApiModal, setIsOpenApiModal] = useState<boolean>(false);
    const [isOpenApiTestModal, setIsOpenApiTestModal] = useState<boolean>(false);

    const handleOpenApiModal = async (rowData: ApiSummary) => {
        const data = await loadApi(projectId, apiSpecificationId, domainId, rowData.apiId).then().catch()
        setApi(data);
        setIsOpenApiModal(true)
    }

    const changeApiModalOpen = (isOpen: boolean) => {
        setIsOpenApiModal(isOpen)
    }

    const handleOpenApiTestModal = async (rowData: ApiSummary) => {
        const data = await loadApi(projectId, apiSpecificationId, domainId, rowData.apiId).then().catch()
        setApi(data);
        setIsOpenApiTestModal(true)
    }

    const changeApiTestModalOpen = (isOpen: boolean) => {
        setIsOpenApiTestModal(isOpen)
    }


    const columns: GridColDef<ApiSummary>[] = [
        {
            field: 'method',
            headerName: 'Method',
            flex: 1,
            renderCell: (params) => (
                <Box
                    // variant="contained"
                    // size="small"
                    sx={{
                        display: 'inline-flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        width: '50%',
                        padding: '6px 20px',
                        fontSize: '0.875rem',
                        fontWeight: '700',
                        lineHeight: '1.75',
                        textTransform: 'uppercase',
                        borderRadius: '4px',
                        backgroundColor: methodColors[`${params.row.method}-BADGE`],
                        color: 'white',
                    }}
                >
                    {params.row.method}
                </Box>

            )
        },
        {
            field: 'name',
            headerName: '이름',
            type: 'string',
            flex: 2,
        },
        {
            field: 'endpoint',
            headerName: 'Endpoint',
            type: 'string',
            flex: 4,
        },
        {
            field: 'test',
            headerName: 'API Test',
            headerAlign: 'center',
            disableColumnMenu: true,
            resizable: false,
            sortable: false,
            align: 'center',
            flex:1,
            renderCell: (params) => (
                <IconButton
                    onClick={() => handleOpenApiTestModal(params.row)}
                >
                    <PlayCircleIcon fontSize='large' style={{color: '#72B545'}}/>
                </IconButton>

            ),
        },
        {
            field: 'detail',
            headerName: '상세',
            headerAlign: 'center',
            disableColumnMenu: true,
            resizable: false,
            sortable: false,
            align: 'center',
            flex:1,
            renderCell: (params) => (
                <Button
                    variant="outlined"
                    size="small"
                    sx={{
                        backgroundColor: '#4F378B',
                        '&:hover': {
                            backgroundColor: '#7854d3'
                        },
                        color: 'white'
                    }}
                    onClick={() => handleOpenApiModal(params.row)}
                >
                    열기
                </Button>

            ),
        },
    ];

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
                    autoHeight={apis.length !== 0}
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
                    getRowClassName={(params) =>
                        `${params.row.method}-ROW`
                    }

                />
            </Box>
            <ApiInfoAndUpdateModal
                projectId={projectId}
                apiSpecificationId={apiSpecificationId}
                domainId={domainId}
                api= {api}
                open={isOpenApiModal}
                onChildChange={onChildChange}
                changeOpen={changeApiModalOpen}
            />
            <ApiTestModal
                api={api}
                open={isOpenApiTestModal}
                changeOpen={changeApiTestModalOpen}
            />

        </>
    )
}

export default ApiListComponent;
