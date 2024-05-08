import {
    Autocomplete,
    Avatar,
    Backdrop,
    Box,
    Button,
    Chip,
    Fade,
    Modal,
    TextField,
    Typography,
    styled,
} from "@mui/material";
import { ProjectInvitationModalProps } from "../../types/Project/Project";
import { useState } from "react";
import { CreateProjectRequest, createProjectApi } from "../../services/Project/ProjectApi";

const ProjectInvitationModalBox = styled(Box)({
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

const Consonant = [
    "ㄱ",
    "ㄲ",
    "ㄴ",
    "ㄷ",
    "ㄸ",
    "ㄹ",
    "ㅁ",
    "ㅂ",
    "ㅃ",
    "ㅅ",
    "ㅆ",
    "ㅇ",
    "ㅈ",
    "ㅉ",
    "ㅊ",
    "ㅋ",
    "ㅌ",
    "ㅍ",
    "ㅎ",
];

const extractConsonant = (input: string) => {
    return input
        .split("")
        .map((char) => {
            const code = char.charCodeAt(0) - 44032;
            // 한글이 아니면 그대로 반환
            if (code < 0 || code > 11171) return char;
            return Consonant[Math.floor(code / 588)];
        })
        .join("");
};

const ProjectInvitationModal = ({ open, onClose, showAlert, setAlertContent }: ProjectInvitationModalProps) => {
    // 서버 멤버 목록을 가져와서 나만 제외하고 보기
    const arr: Array<object> = [
        { id: 1, name: "서버 유저 1", email: "aaa@naver.com" },
        { id: 2, name: "서버 유저 2", email: "bbb@gmail.com" },
        { id: 3, name: "서버 유저 3", email: "ccc@naver.com" },
    ];

    const [projectName, setProjectName] = useState<string | undefined>();

    const createProject = () => {
        const createProjectRequset: CreateProjectRequest = { name: projectName };
        createProjectApi(createProjectRequset)
            .then((data) => {
                setAlertContent({
                    severity: "success",
                    title: "프로젝트 생성 성공!",
                    content: "프로젝트 생성에 성공했습니다.",
                });
                showAlert();
                onClose();
            })
            .catch((error) => {
                if (error.response.status === 400) {
                    setAlertContent({
                        severity: "error",
                        title: "프로젝트 생성 실패!",
                        content: "프로젝트 이름을 확인해주세요.",
                    });
                }
                showAlert();
            });
    };

    // 멤버 초대 로직 추가
    const inviteMembers = () => {};

    return (
        <Modal
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            open={open}
            onClose={onClose}
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
            <Fade in={open}>
                <ProjectInvitationModalBox>
                    <Typography variant="h6">프로젝트 생성</Typography>
                    <TextField
                        required
                        id="standard-required"
                        label="프로젝트 이름"
                        variant="standard"
                        sx={{
                            width: "75%",
                            margin: "16px",
                        }}
                        onChange={(e) => {
                            setProjectName(e.target.value);
                        }}
                    />
                    <Autocomplete
                        multiple
                        autoHighlight
                        id="tags-standard"
                        options={arr}
                        filterOptions={(options, { inputValue }) => {
                            const extractedInputValue = extractConsonant(inputValue);
                            return options.filter((option: any) =>
                                extractConsonant(option.name).includes(extractedInputValue)
                            );
                        }}
                        isOptionEqualToValue={(option: any, value: any) => option.id === value.id}
                        getOptionLabel={(option: any) => option.name}
                        renderOption={(props, option: any) => (
                            <Box component="li" {...props}>
                                <Box
                                    sx={{
                                        display: "flex",
                                        justifyContent: "space-between",
                                        width: "100%",
                                    }}
                                >
                                    <Typography variant="body1">{option.name}</Typography>
                                    <Typography variant="body2" color="textSecondary">
                                        {option.email}
                                    </Typography>
                                </Box>
                            </Box>
                        )}
                        renderTags={(value, getTagProps) =>
                            value.map((option: any, index) => (
                                <Chip
                                    avatar={<Avatar></Avatar>}
                                    label={option.name}
                                    {...getTagProps({ index })}
                                    sx={{ borderRadius: "8px" }}
                                    size="small"
                                />
                            ))
                        }
                        renderInput={(params) => <TextField {...params} variant="standard" label="멤버 추가" />}
                        sx={{
                            width: "75%",
                            marginBottom: "40px",
                        }}
                    />
                    <Box
                        sx={{
                            display: "flex",
                            width: "75%",
                            justifyContent: "flex-end",
                        }}
                    >
                        <Button
                            variant="outlined"
                            sx={{
                                marginInline: "8px",
                            }}
                            onClick={onClose}
                        >
                            취소
                        </Button>

                        <Button
                            variant="outlined"
                            onClick={() => {
                                createProject();
                                // 바로 이 부분에 멤버 초대 로직 추가
                                setProjectName(undefined);
                            }}
                        >
                            프로젝트 생성
                        </Button>
                    </Box>
                </ProjectInvitationModalBox>
            </Fade>
        </Modal>
    );
};

export default ProjectInvitationModal;