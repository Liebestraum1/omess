import Box from "@mui/material/Box";
import {
    Backdrop, Button,
    Modal,
    TextField,
    Typography
} from "@mui/material";

import {ChangeEvent, useEffect, useState} from "react";
import MDEditor from "@uiw/react-md-editor";

type IssueDetailModalProp = {
    open: boolean;
    onClose: () => void;
    issueId: number;
};

const IssueUpdateModal = ({open, onClose}: IssueDetailModalProp) => {
    const [title, setTitle] = useState<string>("");
    const [md, setMd] = useState<string | undefined>("");

    const onCloseModal = () => {
        onClose();
    }
    const handleChangeTitle = (event: ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    };

    // FixMe 이슈 생성  api 호출
    const onClickCreateIssue = () => {
        console.log(title + md);
    }

    // FixMe 이슈 상세 조회 api 호출
    useEffect(() => {
        if(open){
            setTitle(" [Back-End] 회원 가입 api 구현");
            setMd("## 이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기 이메일, 비밀번호,\n" +
                " 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n \n" +
                " 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, \n \n" +
                " - 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n \n" +
                " - 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호, 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기이메일, 비밀번호,\n \n" +
                " 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기 닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 \n \n " +
                "기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 \n \n " +
                "입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, \n \n " +
                "전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, 성별, 전화번호 입력받는 회원가입 기능 구현하기닉네임, \n \n" +
                " 성별, 전화번호 입력받는 회원가입 기능 구현하기")
        }

    }, [open]);

    return (
        <Box>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                open={open}
                onClose={onCloseModal}
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
                    height: "80%",
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    overflowY: 'auto' // 스크롤 활성화
                }}>
                    <Box paddingBottom={2}>
                        <Typography fontWeight="bold" paddingBottom={1}>제목</Typography>
                        <TextField id="standard-basic" onChange={handleChangeTitle} fullWidth defaultValue={title}> </TextField>
                    </Box>
                    <Box sx={{
                        maxHeight: 500, // 최대 높이 설정
                        overflowY: 'auto', // 내용이 많을 경우 스크롤
                        paddingBottom: 5
                    }}>
                        <Typography fontWeight="bold" paddingBottom={1}>설명</Typography>
                        <div className="markarea">
                            <div data-color-mode="light">
                                <MDEditor height={350} value={md} onChange={setMd}/>
                            </div>
                        </div>
                    </Box>
                    <Box display={"flex"} justifyContent={"end"} alignItems={"center"}>
                        <Button variant="outlined" color="secondary" sx={{height: 49, paddingTop: 1.1}}
                                onClick={onClickCreateIssue}> 이슈 수정 </Button>
                    </Box>
                </Box>
            </Modal>
        </Box>
    );
}

export default IssueUpdateModal;
