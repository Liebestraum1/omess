import {findMemberByEmail, findMemberById} from "../../services/member/MemberApi.ts";
import {Alert, Backdrop, Button, Chip, Fade, Modal, TextField, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {useEffect, useRef, useState} from "react";
import styled from "@mui/system/styled";
import {useSignInStore} from "../../stores/SignInStorage.tsx";
import {chatCreate} from "../../services/chat/ChatApi.ts";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import {useNavigate} from "react-router-dom";

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

const ChatCreationModal = ({open, setOpen, projectId}: { open: boolean, setOpen: any, projectId: number}) => {
    const {addChat} = useChatStorage();
    const eMessage = '유효한 이메일 주소를 입력해 주세요.';
    const {memberId} = useSignInStore();
    const emailRef = useRef<HTMLInputElement>(null);
    const [user, setUser] = useState<string>('');
    const [isAlert, setIsAlert] = useState<boolean>(false);
    const [chatName, setChatName] = useState<string>('');
    const [isEmailInvalid, setIsEmailInvalid] = useState<boolean>(false);
    const [errorMessage, setErrorMessage] = useState<string>(eMessage);
    const [members, setMembers] = useState<Array<string> | null>([]);

    useEffect(() => {
        if (memberId == undefined) return;
        findMemberById(memberId!).then(
            (data) => {
                setMembers([]);
                const member = data[0];
                setUser(member.email);
            }
        );
    }, []);
    const searchMember = () => {
        const email = emailRef.current!.value
        if (isEmailInvalid || email.trim().length < 1) return;
        if (!members!.includes(email)) {
            findMemberByEmail(email).then((data) => {
                if (data.length < 1) {
                    setErrorMessage('유효하지 않은 회원입니다.')
                    setIsEmailInvalid(true);
                } else {
                    emailRef.current!.value = ''
                    setMembers(prevState => [...prevState!, email]);
                    setErrorMessage(eMessage)
                    setIsEmailInvalid(false);
                }
            })
        } else {
            setErrorMessage('이미 존재하는 회원입니다.')
            setIsEmailInvalid(true);
        }
    }

    const validateEmail = (email: string) => {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    const handleEmailChange = (event: any) => {
        const email = event.target.value;
        setIsEmailInvalid(!validateEmail(email));
    }

    const navigator = useNavigate();
    const makeChat = () => {
        if (members!.length < 1 || chatName.trim().length < 1) {
            setIsAlert(true);
        } else {
            setIsAlert(false);
            setMembers([]);
            setOpen(false);
            const data = {name: chatName, emails: members};
            chatCreate(projectId, JSON.stringify(data)).then((response) => {
                const {data} = response
                addChat(data);
            })
        }
    }

    const handleDelete = (value: string) => {
        setMembers(members!.filter(m => m !== value));
    }

    const close = () => {
        setIsAlert(false);
        setMembers([]);
        setOpen(false);
    }

    return (
        <Modal
            open={open}
            onClose={close}
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            closeAfterTransition
            slots={{backdrop: Backdrop}}
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
                <ModuleCreationModalBox>
                    <Box width='90%'>
                        {
                            isAlert ? <Alert variant="filled" severity="error">
                                이름과 초대 사용자 정보를 확인해 주세요
                            </Alert> : null
                        }
                    </Box>
                    <Typography variant="h6">채팅 생성</Typography>
                    <Box display='flex' justifyContent='start' width='80%'>
                        <TextField
                            required
                            id="standard-required"
                            label="채팅 이름"
                            variant="standard"
                            onChange={(e) => setChatName(e.target.value)}
                            sx={{width: '100%'}}
                        />
                    </Box>
                    <Box display='flex' mt={2} gap={2} justifyContent='space-between' width='80%'>
                        <TextField
                            inputRef={emailRef}
                            required
                            label="사용자 검색"
                            variant="standard"
                            onChange={handleEmailChange}
                            error={isEmailInvalid} // 유효하지 않은 이메일일 경우 에러 표시
                            helperText={isEmailInvalid ? errorMessage : ''}
                            sx={{width: '100%'}}
                        />
                        <Button onClick={() => searchMember()} color='secondary' variant='contained'>추가</Button>
                    </Box>
                    <Box display="flex" flexWrap="wrap" gap={1} my={1} p={2}
                         sx={{
                             width: '80%', // 너비 설정
                             '.MuiChip-root': {
                                 flex: '1 1 calc(33.333% - 8px)' // 3개씩 분할, gap 고려
                             }
                         }}
                    >
                        <Chip
                            label={user}
                            variant="outlined"
                            sx={{
                                width: '100%',
                                overflow: 'hidden',
                                whiteSpace: 'nowrap',
                                textOverflow: 'ellipsis'
                            }}
                        />
                        {members?.map((value, key) => (
                            <Chip
                                key={key}
                                label={value}
                                variant="outlined"
                                sx={{
                                    width: '100%',
                                    overflow: 'hidden',
                                    whiteSpace: 'nowrap',
                                    textOverflow: 'ellipsis'
                                }}
                                onDelete={() => handleDelete(value)}
                            />
                        ))}
                    </Box>
                    <Button
                        variant='contained'
                        color='secondary'
                        onClick={makeChat}
                    >
                        생성
                    </Button>
                </ModuleCreationModalBox>
            </Fade>
        </Modal>
    );
}

export default ChatCreationModal;