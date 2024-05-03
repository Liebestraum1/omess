import Box from "@mui/material/Box";
import {
    Backdrop, Button,
    Divider, Menu, MenuItem,
    Modal,
    Rating,
    Typography
} from "@mui/material";
import {IssueDetailProp} from "../../../types/Issue/IssueDetail.ts";
import IssueLabelFilter from "./IssueLabelFilter.tsx";
import IssueChargerFilter from "./IssueChargerFilter.tsx";
import Grid from '@mui/material/Grid';
import MDEditor from "@uiw/react-md-editor";
import IssueStatusFilter from "./IssueStatusFilter.tsx";
import {useState} from "react";
import IssueUpdateModal from "../IssueUpdate/IssueUpdateModal.tsx";
import MoreVertIcon from '@mui/icons-material/MoreVert';

type IssueDetailModalProp = {
    open: boolean;
    issueId: number;
    onClose: () => void;
};

const IssueDetailModal = ({open, issueId, onClose}: IssueDetailModalProp) => {
    const [openUpdate, setOpenUpdate] = useState(false);
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    // 메뉴 여닫기
    const handleClickOpenMenu = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleCloseMenu = () => {
        setAnchorEl(null);
    };

    // 수정하기

    const handleClickOpen = () => {
        handleCloseMenu();
        onClose();
        setOpenUpdate(true);
    }

    const handleClose = () => {
        setOpenUpdate(false);
    }
    
    
    //삭제하기
    const handleClickDelete = () => {
        handleCloseMenu();
        // FixMe 이슈 삭제 api 호출
    }

    // FixMe 이슈 디테일 api 호출
    const issueDetail: IssueDetailProp = {
        issueId: 1,
        title: " [Back-End] 회원 가입 api 구현",
        content: "## 이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기 이메일, 비밀번호,\n" +
            " 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n \n" +
            " 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, \n \n" +
            " - 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n \n" +
            " - 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n \n" +
            " 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 \n \n " +
            "기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 \n \n " +
            "입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, \n \n " +
            "전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, \n \n" +
            " 성별, 전화번호 입력받는 회원가입 기능 구현하기",
        charger: {
            id: 1,
            nickname: "슈밤",
            profile: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"
        },
        label: {
            labelId: 1,
            name: "Back-End"
        },
        importance: 1,
        status: 1,
    }

    const onClickRating = () => {
        // FixMe 이슈 중요도 수정 api 호출
    }

    return (
        <Box>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                open={open}
                onClose={onClose}
                closeAfterTransition
                slots={{backdrop: Backdrop}}
                slotProps={{
                    backdrop: {
                        timeout: 500,
                    },
                }}
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: "80%",
                    height: "70%",
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    overflowY: 'auto' // 스크롤 활성화
                }}>
                    <Box display={"flex"} justifyContent={"space-between"}>
                        <Typography variant="h4" paddingBottom={3}> {issueId} {issueDetail.title} </Typography>
                        <Box>
                            <Button
                                id="basic-button"
                                aria-controls={open ? 'basic-menu' : undefined}
                                aria-haspopup="true"
                                aria-expanded={open ? 'true' : undefined}
                                onClick={handleClickOpenMenu}
                            >
                                <MoreVertIcon color="secondary"/>
                            </Button>
                            <Menu
                                id="basic-menu"
                                anchorEl={anchorEl}
                                open={Boolean(anchorEl)}
                                onClose={handleCloseMenu}
                                MenuListProps={{
                                    'aria-labelledby': 'basic-button',
                                }}
                            >
                                <MenuItem onClick={handleClickOpen}>수정하기</MenuItem>
                                <MenuItem onClick={handleClickDelete}>삭제하기</MenuItem>
                            </Menu>
                        </Box>
                    </Box>
                    <Divider/>
                    <Grid container paddingY={2}>
                        <Grid xs={10}>
                            <Box sx={{
                                overflowY: 'auto' // 내용이 많을 경우 스크롤
                            }}>
                                <Typography fontWeight="bold" paddingBottom={1}>설명</Typography>
                                <div className="markarea">
                                    <div data-color-mode="light">
                                        <MDEditor.Markdown source={issueDetail.content}/>
                                    </div>
                                </div>
                            </Box>
                        </Grid>
                        <Grid xs={2} paddingLeft={1}>
                            <Box>
                                <IssueChargerFilter id={issueDetail.charger.id}/>
                                <IssueLabelFilter labelId={issueDetail.label.labelId}/>
                                <IssueStatusFilter status={issueDetail.status}/>
                                <Box paddingLeft={5.5}>
                                    <Rating name="read-only" size="large" defaultValue={issueDetail.importance}
                                            onChange={onClickRating} max={3}/>
                                </Box>
                            </Box>
                        </Grid>
                    </Grid>
                </Box>
            </Modal>

            <IssueUpdateModal open={openUpdate} onClose={handleClose} issueId={issueId}/>
        </Box>
    );
}

export default IssueDetailModal;
