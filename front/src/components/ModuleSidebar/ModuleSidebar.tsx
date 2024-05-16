import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import Module from "./Module.tsx";
import { useProjectStore } from "../../stores/ProjectStorage.tsx";
import { useEffect, useState } from "react";
import {
    createModuleApi,
    getModuleCategoryApi,
    getModulesApi,
    ModuleCategoryResponse,
    ModuleCreateRequest,
    ModuleResponse,
} from "../../services/Module/ModuleApi.ts";
import AddIcon from "@mui/icons-material/Add";
import {
    Alert,
    AlertTitle,
    Backdrop,
    Button,
    Collapse,
    Fade,
    FormControl,
    FormHelperText,
    InputLabel,
    Menu,
    MenuItem,
    Modal,
    Select,
    Snackbar,
    TextField,
    Typography,
} from "@mui/material";
import { ChatAccordion } from "../chat/ChatAccordion.tsx";
import { Link } from "react-router-dom";
import ArrowDropDown from "@mui/icons-material/ArrowDropDown";
import ArrowRight from "@mui/icons-material/ArrowRight";
import { getProjectApi, leaveProjectApi } from "../../services/Project/ProjectApi.ts";
import { Project } from "../../types/Project/Project.ts";
import MemberInvitationModal from "./MemberInvitationModal.tsx";
import { AlertContent } from "../../types/common/Alert.ts";

type GroupModules = {
    [key: string]: ModuleResponse[];
};

const ModuleCreationModalBox = styled(Box)({
    width: "425px",
    height: "325px",
    backgroundColor: "white",
    position: "absolute",
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    borderRadius: "16px",
});

const ModuleSidebarTitleBox = styled(Box)({
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    paddingInline: "16px",
    height: "48px",
    // backgroundColor: "#dbc6f7",
    whiteSpace: "nowrap",
    padding: "0px",
});

const ModuleSidebarTitle = styled(Typography)({
    color: "#49454F",
});

const ModuleSidebarBox = styled(Box)({
    width: 200,
    backgroundColor: "#E8DEF8",
    overflow: "auto",

    "&::-webkit-scrollbar": {
        width: "8px", // 스크롤바의 너비
    },
    "&::-webkit-scrollbar-track": {
        background: "transparent",
    },
    "&::-webkit-scrollbar-thumb": {
        display: "none",
        backgroundColor: "purple", // 스크롤바 썸(움직이는 부분)의 배경색
        borderRadius: "4px", // 스크롤바 썸의 모서리 둥글기
    },
});

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const ModuleSidebar = () => {
    const { selectedProjectName, selectedProjectId, setSelectedProjectName, setSelectedProjectId, setProjectList } =
        useProjectStore();

    const [groupedModules, setGroupedModules] = useState<GroupModules>({});
    const [openModuleCreationModal, setOpenModuleCreationModal] = useState<boolean>(false);

    const [moduleName, setModuleName] = useState<string>("");
    const [moduleCategory, setModuleCategory] = useState<Array<ModuleCategoryResponse>>([]);
    const [selectedModuleCategory, setSelectedModuleCategory] = useState<number | "">("");

    const [openModule, setOpenModule] = useState<boolean>(false);
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    const handleProjectMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleProjectMenuClose = () => {
        setAnchorEl(null);
    };

    const createModule = (index: number) => {
        const requestCategory = moduleCategory[index];
        const moduleCreateRequest: ModuleCreateRequest = { name: moduleName, category: requestCategory.category };
        if (selectedProjectId) {
            createModuleApi(selectedProjectId, requestCategory.path, moduleCreateRequest).then((data) => {
                setSelectedModuleCategory("");
                setOpenModuleCreationModal(false);
            });
        }
    };

    const [showAlert, setShowAlert] = useState<boolean>(false);
    const [modalOpen, setModalOpen] = useState(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    const leaveProject = () => {
        if (selectedProjectId != undefined) {
            leaveProjectApi(selectedProjectId).then(() => {
                setSelectedProjectId(undefined);
                setSelectedProjectName(undefined);
                getProjectList();
            });
        }
    };

    const getProjectList = () => {
        getProjectApi()
            .then((data: any) => {
                const dataList: Array<Project> = data["projects"];
                setProjectList(dataList);
            })
            .catch((error) => {});
    };

    const handleModule = () => {
        setOpenModule(!openModule);
    };

    useEffect(() => {
        if (selectedProjectId) {
            getModuleCategoryApi(selectedProjectId).then((data: any) => {
                setModuleCategory(data);
            });
        }
    }, [selectedProjectId]);

    useEffect(() => {
        if (selectedProjectId != undefined) {
            getModulesApi(selectedProjectId).then((data: any) => {
                const modules = data.reduce((acc: any, module: ModuleResponse) => {
                    const key = module.category;
                    if (!acc[key]) {
                        acc[key] = [];
                    }

                    acc[key].push(module);
                    return acc;
                }, {});
                setGroupedModules(modules);
            });
        }
    }, [selectedProjectId, openModuleCreationModal]);

    return (
        <ModuleSidebarBox>
            {selectedProjectId === undefined ? null : (
                <>
                    <ModuleSidebarTitleBox>
                        <Button
                            fullWidth
                            sx={{
                                display: "flex",
                                justifyContent: "flex-start",
                                height: "100%",
                            }}
                            onClick={handleProjectMenu}
                        >
                            <ModuleSidebarTitle variant="h6">{selectedProjectName} </ModuleSidebarTitle>
                        </Button>
                    </ModuleSidebarTitleBox>
                    <Menu
                        id="fade-menu"
                        anchorEl={anchorEl}
                        open={Boolean(anchorEl)}
                        onClose={handleProjectMenuClose}
                        TransitionComponent={Fade}
                    >
                        <MenuItem
                            onClick={() => {
                                handleProjectMenuClose();
                                setModalOpen(true);
                            }}
                        >
                            프로젝트 멤버 추가
                        </MenuItem>
                        <MenuItem
                            onClick={() => {
                                handleProjectMenuClose();
                                leaveProject();
                            }}
                        >
                            프로젝트 나가기
                        </MenuItem>
                    </Menu>
                    <ModuleSidebarTitleBox
                        sx={{
                            backgroundColor: "#dbc6f7",
                            paddingInline: "0px",
                        }}
                    >
                        <Box
                            onClick={handleModule}
                            sx={{
                                display: "flex",
                                width: "100%",
                                cursor: "pointer",
                            }}
                        >
                            {openModule ? <ArrowDropDown color="action" /> : <ArrowRight color="action" />}
                            <Typography>모듈</Typography>
                        </Box>
                        <AddIcon
                            onClick={() => setOpenModuleCreationModal(true)}
                            sx={{ cursor: "pointer", padding: "8px" }}
                        ></AddIcon>
                    </ModuleSidebarTitleBox>
                    <Collapse in={openModule} timeout="auto" unmountOnExit>
                        {Object.keys(groupedModules).length != 0 ? (
                            Object.keys(groupedModules).map((key) => (
                                <Module
                                    moduleCategory={key}
                                    key={key}
                                    moduleItems={groupedModules[key].map((module: ModuleResponse) => module)}
                                ></Module>
                            ))
                        ) : (
                            <Typography margin={"8px"}> 모듈이 비어있습니다. </Typography>
                        )}
                    </Collapse>

                    <MemberInvitationModal
                        open={modalOpen}
                        onClose={() => setModalOpen(false)}
                        showAlert={() => setShowAlert(true)}
                        setAlertContent={(alertContent: AlertContent) => setAlertContent(alertContent)}
                    ></MemberInvitationModal>
                    <Snackbar
                        open={showAlert}
                        anchorOrigin={{ horizontal: "center", vertical: "bottom" }}
                        autoHideDuration={3000}
                        onClose={() => setShowAlert(false)}
                    >
                        <Alert severity={alertContent.severity} action={alertButton(() => setShowAlert(false))}>
                            <AlertTitle>{alertContent.title}</AlertTitle>
                            {alertContent.content}
                        </Alert>
                    </Snackbar>

                    <ChatAccordion projectId={selectedProjectId} />
                </>
            )}

            <Modal
                open={openModuleCreationModal}
                onClose={() => setOpenModuleCreationModal(false)}
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                closeAfterTransition
                slots={{ backdrop: Backdrop }}
                slotProps={{
                    backdrop: {
                        timeout: 500,
                    },
                }}
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <Fade in={openModuleCreationModal}>
                    <ModuleCreationModalBox>
                        <Typography variant="h6">모듈 생성</Typography>
                        <TextField
                            required
                            id="standard-required"
                            label="모듈 이름"
                            variant="standard"
                            sx={{
                                width: "75%",
                                margin: "16px",
                            }}
                            onChange={(e) => {
                                setModuleName(e.target.value);
                            }}
                        />
                        <FormControl required variant="standard" sx={{ width: "75%" }}>
                            <InputLabel id="demo-simple-select-required-label">모듈 종류</InputLabel>
                            <Select
                                labelId="demo-simple-select-standard-label"
                                id="demo-simple-select-standard"
                                value={selectedModuleCategory}
                                label="생성할 모듈의 종류를 선택해주세요. "
                                onChange={(e: any) => {
                                    const index = parseInt(e.target.value);
                                    setSelectedModuleCategory(index);
                                }}
                            >
                                {moduleCategory?.map((data, index) => (
                                    <MenuItem value={index} key={data.id}>
                                        {data.category}
                                    </MenuItem>
                                ))}
                            </Select>
                            <FormHelperText>생성할 모듈의 종류를 선택해주세요.</FormHelperText>
                        </FormControl>
                        <Button
                            onClick={() => {
                                if (selectedModuleCategory !== "") {
                                    createModule(selectedModuleCategory);
                                }
                            }}
                        >
                            생성
                        </Button>
                    </ModuleCreationModalBox>
                </Fade>
            </Modal>
        </ModuleSidebarBox>
    );
};

export default ModuleSidebar;
