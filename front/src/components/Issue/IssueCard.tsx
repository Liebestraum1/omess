import {Avatar, Button, Card, CardContent, Chip, Rating, Typography} from "@mui/material";
import Grid from '@mui/material/Grid';
import Box from "@mui/material/Box";
import {IssueProp} from "../../types/Issue/Issue.ts";
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";
import {useEffect, useState} from "react";
import {ColorArr} from "../../services/common/Color.ts";

type IssueCardProp = {
    issue: IssueProp;
    handleClickOpen: () => void;
}

const IssueCard = ({issue, handleClickOpen}: IssueCardProp) => {
    const {setIssuedId} = useKanbanBoardStore();
    const [colorArr, setColorArr] = useState<string[]>([]);
    useEffect(() => {
        setColorArr(ColorArr);
    }, [setColorArr]);
    return (
        <Card>
            <CardContent>
                <Grid container>
                    <Grid item xs={9}>
                        <Box display={"flex"} flexDirection={"column"}>
                            <Typography style={{paddingBottom: 10}}> {issue.title} </Typography>
                            <Rating style={{paddingBottom: 10}} readOnly name="read-only"
                                    value={issue.importance}
                                    max={3}/>
                            {issue.label && (
                                <Chip label={issue.label.name} sx={{
                                    width: 80,
                                    borderColor: colorArr[issue.label.labelId % 20], // 테두리 색상
                                    backgroundColor: colorArr[issue.label.labelId % 20],
                                    '&:hover': {
                                        backgroundColor: colorArr[issue.label.labelId % 20], // 호버 시 배경 색상 추가
                                        color: 'white' // 호버 시 글자 색상 변경
                                    }
                                }}/>
                            )}
                        </Box>
                    </Grid>
                    <Grid item xs={3}>
                        <Box display={"flex"} flexDirection={"column"} justifyContent="center"
                             alignItems="center">
                            <Avatar alt="프로필 이미지" src={issue.charger?.profile || ""}/>
                            <Box style={{paddingBottom: 30}}></Box>
                            <Button size="small" color="secondary" onClick={() => {
                                setIssuedId(issue.issueId);
                                handleClickOpen()
                            }}>더보기</Button>
                        </Box>
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}

export default IssueCard;
