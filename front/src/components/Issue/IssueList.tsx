import {Button, Stack, Typography} from "@mui/material";
import Grid from "@mui/material/Unstable_Grid2";
import Box from "@mui/material/Box";
import {IssueProp} from "../../types/Issue/Issue.ts";
import IssueCard from "./IssueCard.tsx";

type IssueListProp = {
    title: string;
    issues: IssueProp[];
    handleClickOpen: (issueId:number) => void;
};

const IssueList = ({title, issues, handleClickOpen}: IssueListProp) => {
    return (
        <Grid xs={3.8}>
            <Box style={{backgroundColor: "#F7F8F9"}} padding={1} height={"100%"}>
                <Typography style={{padding: 10}}> {title} </Typography>
                <Stack spacing={3}>
                    {issues.map((issue) => (
                        <IssueCard issue={issue} handleClickOpen={handleClickOpen}/>
                    ))}

                </Stack>
                <Box padding={1} display={"flex"} justifyContent="center" alignItems="center">
                    <Button>+ 이슈 만들기</Button>
                </Box>
            </Box>
        </Grid>
    );
}

export default IssueList;