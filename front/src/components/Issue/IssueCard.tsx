import {Avatar, Button, Card, CardContent, Chip, Rating, Typography} from "@mui/material";
import Grid from "@mui/material/Unstable_Grid2";
import Box from "@mui/material/Box";
import {IssueProp} from "../../types/Issue/Issue.ts";

type IssueCardProp = {
    issue: IssueProp;
    handleClickOpen: (issueId: number) => void;
}

const IssueCard = ({issue, handleClickOpen}: IssueCardProp) => {
    return (
        <Card>
            <CardContent>
                <Grid container>
                    <Grid xs={9}>
                        <Box display={"flex"} flexDirection={"column"}>
                            <Typography style={{paddingBottom: 10}}> {issue.title} </Typography>
                            <Rating style={{paddingBottom: 10}} readOnly name="read-only"
                                    value={issue.importance}
                                    max={3}/>
                            <Chip label={issue.label.name} color="primary" sx={{width: 80}}/>
                        </Box>
                    </Grid>
                    <Grid xs={3}>
                        <Box display={"flex"} flexDirection={"column"} justifyContent="center"
                             alignItems="center">
                            <Avatar alt="프로필 이미지" src={issue.charger.profile}/>
                            <Box style={{paddingBottom: 30}}></Box>
                            <Button size="small" onClick={() => {
                                handleClickOpen(issue.issueId)
                            }}>더보기</Button>
                        </Box>
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}

export default IssueCard;