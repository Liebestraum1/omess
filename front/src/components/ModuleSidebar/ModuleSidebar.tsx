import Box from "@mui/material/Box";
import styled from "@mui/system/styled";
import Module from "./Module.tsx";
import { useProjectStore } from "../../stores/ProjectStorage.tsx";
import { useEffect, useState } from "react";
import {
    ModuleCategoryResponse,
    ModuleCreateRequest,
    ModuleResponse,
    createModuleApi,
    getModuleCategoryApi,
    getModulesApi,
} from "../../services/Module/ModuleApi.ts";
import AddIcon from "@mui/icons-material/Add";
import {
    Backdrop,
    Button,
    Fade,
    FormControl,
    FormHelperText,
    InputLabel,
    MenuItem,
    Modal,
    Select,
    TextField,
    Typography,
} from "@mui/material";

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
    paddingTop: "12px",
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

const ModuleSidebar = () => {
    const { selectedProjectName, selectedProjectId } = useProjectStore();
    const [groupedModules, setGroupedModules] = useState<GroupModules>({});
    const [openModuleCreationModal, setOpenModuleCreationModal] = useState<boolean>(false);
    const [moduleName, setModuleName] = useState<string>("");
    const [moduleCategory, setModuleCategory] = useState<Array<ModuleCategoryResponse>>([]);
    const [selectedModuleCategory, setSelectedModuleCategory] = useState<number | "">("");

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
                <ModuleSidebarTitleBox>
                    <ModuleSidebarTitle variant="h6">{selectedProjectName} </ModuleSidebarTitle>
                    <AddIcon onClick={() => setOpenModuleCreationModal(true)} sx={{ cursor: "pointer" }}></AddIcon>
                </ModuleSidebarTitleBox>
            )}

            {Object.keys(groupedModules).map((key) => (
                <Module
                    moduleCategory={key}
                    key={key}
                    moduleItems={groupedModules[key].map((module: ModuleResponse) => module.title)}
                ></Module>
            ))}
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
