import * as React from 'react';
import Box from "@mui/material/Box";
import {Autocomplete, Backdrop, Button, Modal, Snackbar, TextField, Grid, Chip} from "@mui/material";
import {useEffect, useState} from 'react';
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";
import CancelIcon from '@mui/icons-material/Cancel';
import {ColorArr} from "../../services/common/Color.ts";


type LabelManageModalProp = {
    open: boolean;
    onClose: () => void;
}

const LabelManageModal = ({open, onClose}: LabelManageModalProp) => {
    const {currentProjectId, kanbanBoardId, labels, createLabel, deleteLabel} = useKanbanBoardStore();
    const [inputValue, setInputValue] = useState('');
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [message, setMessage] = useState('');
    const [colorArr, setColorArr] = useState<string[]>([]);

    useEffect(() => {
        setColorArr(ColorArr);
    }, [setColorArr]);

    const handelCreateLabel = () => {
        const exists = labels.some(label => label.name.toLowerCase() === inputValue.toLowerCase());
        if (exists) {
            setMessage('이미 존재하는 라벨입니다.');
            setSnackbarOpen(true);
        } else if (!inputValue) {
            setMessage('라벨 이름은 공백일 수 없습니다.');
            setSnackbarOpen(true);
        } else {
            if (currentProjectId && kanbanBoardId && inputValue) {
                createLabel(currentProjectId, kanbanBoardId, inputValue);
            }
        }
    }

    return (
        <Box>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                open={open}
                onClose={onClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: "50%",
                    height: "50%",
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    overflowY: 'auto'
                }}>
                    <Box>
                        <Grid container spacing={2}>
                            <Grid item xs={10}>
                                <Autocomplete
                                    renderInput={(params) => <TextField {...params} label=""/>}
                                    renderOption={(props, option) => <li {...props}>{option.name}</li>}
                                    getOptionLabel={(option) => typeof option === 'string' ? option : option.name}
                                    options={labels}
                                    freeSolo
                                    onInputChange={(event, newInputValue) => {
                                        setInputValue(newInputValue);
                                    }}
                                />
                            </Grid>
                            <Grid item xs={2} sx={{display: 'flex', justifyContent: 'center'}}>
                                <Button
                                    color="secondary"
                                    onClick={handelCreateLabel}
                                    disabled={!inputValue.trim()}  // 입력값이 비어있지 않아야 버튼 활성화
                                >
                                    라벨 생성
                                </Button>
                            </Grid>
                        </Grid>
                    </Box>
                    <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 2, marginTop: 4}}>
                        {labels.map((label) => (
                            <Chip
                                key={label.labelId}
                                label={label.name}
                                // variant="outlined"
                                onDelete={() => {
                                    if (currentProjectId && kanbanBoardId) {
                                        deleteLabel(currentProjectId, kanbanBoardId, label.labelId);
                                    }
                                }}
                                deleteIcon={<CancelIcon/>}
                                sx={{
                                    borderColor: colorArr[label.labelId % 20], // 테두리 색상
                                    backgroundColor: colorArr[label.labelId % 20],
                                    '&:hover': {
                                        backgroundColor: colorArr[label.labelId % 20], // 호버 시 배경 색상 추가
                                        color: 'white' // 호버 시 글자 색상 변경
                                    }
                                }}
                            />
                        ))}
                    </Box>
                </Box>
            </Modal>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={() => setSnackbarOpen(false)}
                message={message}
                action={
                    <React.Fragment>
                        <Button color="secondary" size="small" onClick={() => setSnackbarOpen(false)}>
                            닫기
                        </Button>
                    </React.Fragment>
                }
            />
        </Box>
    );
}

export default LabelManageModal;
