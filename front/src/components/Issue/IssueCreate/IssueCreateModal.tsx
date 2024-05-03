import Box from "@mui/material/Box";
import {
    Backdrop, Button,
    Modal,
    TextField,
    Typography
} from "@mui/material";

import {ChangeEvent, useState} from "react";
import MDEditor from "@uiw/react-md-editor";
import IssueCreateStatusFilter from "./IssueCreateStatusFilter.tsx";
import IssueCreateImportanceFilter from "./IssueCreateImportanceFilter.tsx";
import IssueCreateChargerFilter from "./IssueCreateChargerFilter.tsx";
import IssueCreateLabelFilter from "./IssueCreateLabelFilter.tsx";

type IssueDetailModalProp = {
    open: boolean;
    onClose: () => void;
};

const IssueCreateModal = ({open, onClose}: IssueDetailModalProp) => {
    const [title, setTitle] = useState<string>("");
    const [md, setMd] = useState<string | undefined>("");
    const [status, setStatus] = useState<number | null>(null);
    const [importance, setImportance] = useState<number | null>(null);
    const [charger, setCharger] = useState<number | null>(null);
    const [label, setLabel] = useState<number | null>(null);

    const onCloseModal = () => {
        setMd("");
        onClose();
    }
    const handleChangeTitle = (event: ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    };

    // FixMe 이슈 생성  api 호출
    const onClickCreateIssue = () => {
        console.log(title + md + charger + label + importance + status);
    }
    
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
                        <TextField id="standard-basic" onChange={handleChangeTitle} fullWidth> </TextField>
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
                    <Box display={"flex"} justifyContent={"space-between"} alignItems={"center"}>
                        <Box display={"flex"}>
                            <IssueCreateChargerFilter onChangeCharger={setCharger}/>
                            <IssueCreateLabelFilter onChangeLabel={setLabel}/>
                            <IssueCreateImportanceFilter onChangeImportance={setImportance}/>
                            <IssueCreateStatusFilter onChangeStatus={setStatus} />
                        </Box>
                        <Button variant="outlined" color="secondary"  sx={{height: 49, paddingTop: 1.1}} onClick={onClickCreateIssue}> 이슈 생성 </Button>
                    </Box>
                </Box>
            </Modal>
        </Box>
    );
}

export default IssueCreateModal;
