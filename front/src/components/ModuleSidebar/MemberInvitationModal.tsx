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
import { useEffect, useState } from "react";
import { InviteProjectRequest, Member, getMemberApi, inviteProjectApi } from "../../services/Project/ProjectApi";
import { useProjectStore } from "../../stores/ProjectStorage";

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

const MemberInvitationModal = ({ open, onClose, showAlert, setAlertContent }: ProjectInvitationModalProps) => {
    // 서버 멤버 목록을 가져와서 나만 제외하고 보기
    const { selectedProjectId, selectedProjectName } = useProjectStore();
    const [memberList, setMemberList] = useState<Array<Member>>([]);
    const [invitationList, setInviationList] = useState<Array<number>>([]);

    useEffect(() => {
        getMemberApi().then((data: any) => {
            setMemberList(data);
        });
    }, []);

    const inviteMembers = () => {
        if (selectedProjectId != undefined) {
            const inviteProectRequest: InviteProjectRequest = {
                inviteMembers: invitationList,
            };
            inviteProjectApi(selectedProjectId, inviteProectRequest)
                .then((data: any) => {
                    setAlertContent({
                        severity: "success",
                        title: "멤버 초대 성공!",
                        content: "새로운 멤버들을 프로젝트에 초대했습니다.",
                    });
                    showAlert();
                    onClose();
                })
                .catch((error) => {
                    if (error.response.status === 400) {
                        setAlertContent({
                            severity: "error",
                            title: "멤버 초대 실패!",
                            content: "멤버 목록을 확인해주세요.",
                        });
                    }
                    showAlert();
                });
        }
    };

    const addMemberToInvitationList = (event: any, value: Array<Member>) => {
        setInviationList(value.map((member) => member.id));
    };

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
                    <Typography variant="h6">프로젝트 멤버 추가</Typography>
                    <TextField
                        disabled
                        id="standard-disabled"
                        label="프로젝트 이름"
                        variant="standard"
                        defaultValue={selectedProjectName}
                        sx={{
                            width: "75%",
                            margin: "16px",
                        }}
                    />
                    <Autocomplete
                        multiple
                        autoHighlight
                        id="tags-standard"
                        options={memberList}
                        filterOptions={(options, { inputValue }) => {
                            const extractedInputValue = extractConsonant(inputValue);
                            return options.filter(
                                (option: any) =>
                                    extractConsonant(option.nickname).includes(extractedInputValue) ||
                                    extractConsonant(option.email).includes(extractedInputValue)
                            );
                        }}
                        isOptionEqualToValue={(option: any, value: any) => option.id === value.id}
                        getOptionLabel={(option: any) => option.nickname}
                        onChange={addMemberToInvitationList}
                        renderOption={(props, option: any) => (
                            <Box component="li" {...props}>
                                <Box
                                    sx={{
                                        display: "flex",
                                        justifyContent: "space-between",
                                        width: "100%",
                                    }}
                                >
                                    <Typography variant="body1">{option.nickname}</Typography>
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
                                    label={option.nickname}
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
                                inviteMembers();
                            }}
                        >
                            멤버 초대
                        </Button>
                    </Box>
                </ProjectInvitationModalBox>
            </Fade>
        </Modal>
    );
};

export default MemberInvitationModal;
