import Box from "@mui/material/Box";
import {
    Backdrop,
    Divider,
    Modal,
    Rating,
    Typography
} from "@mui/material";
import Grid from "@mui/material/Unstable_Grid2";
import {IssueDetailProp} from "../../types/Issue/IssueDetail.ts";
import IssueLabelFilter from "./IssueLabelFilter.tsx";
import IssueChargerFilter from "./IssueChargerFilter.tsx";

type IssueDetailModalProp = {
    open: boolean;
    issueId: number;
    onClose: () => void;
};

const IssueDetailModal = ({open, issueId, onClose}: IssueDetailModalProp) => {

    // FixMe 이슈 디테일 api 호출
    const issueDetail: IssueDetailProp = {
        issueId: 1,
        title: " [Back-End] 회원 가입 api 구현",
        content: "이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기 이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n" +
            "                                    닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기",
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
                    height: "60%",
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    overflowY: 'auto' // 스크롤 활성화
                }}>
                    <Box paddingBottom={2}>
                        <Typography variant="h4" paddingBottom={3}> {issueId} {issueDetail.title} </Typography>
                        <Divider/>
                    </Box>
                    <Grid container>
                        <Grid xs={10}>
                            <Box sx={{
                                maxHeight: 500, // 최대 높이 설정
                                overflowY: 'auto' // 내용이 많을 경우 스크롤
                            }}>
                                <Typography fontWeight="bold" paddingBottom={1}>설명</Typography>
                                <Typography paddingBottom={1}> {issueDetail.content} </Typography>
                            </Box>
                        </Grid>
                        <Grid xs={2} paddingLeft={1}>
                            <Box>
                                <IssueChargerFilter id={issueDetail.charger.id}/>
                                <IssueLabelFilter labelId={issueDetail.label.labelId}/>
                                <Rating style={{paddingLeft: 18}} name="read-only" size="large"
                                        defaultValue={issueDetail.importance} onChange={onClickRating} max={3}/>
                            </Box>
                        </Grid>
                    </Grid>
                </Box>
            </Modal>
        </Box>
    );
}

export default IssueDetailModal;
