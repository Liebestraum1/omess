import {Button, Stack, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {IssueProp} from "../../types/Issue/Issue.ts";
import IssueCard from "./IssueCard.tsx";
import Grid from '@mui/material/Grid';

type IssueListProp = {
    title: string;
    issues: IssueProp[];
    handleClickOpen: (issueId:number) => void;
    handleClickOpenCreate: () => void;
};

const IssueList = ({title, issues, handleClickOpen, handleClickOpenCreate}: IssueListProp) => {

    return (
        <Grid item xs={3.8}>
            <Box style={{backgroundColor: "#F7F8F9"}} padding={1} height={"100%"}>
                <Typography style={{padding: 10}}> {title} </Typography>
                <Stack spacing={3}>
                    {issues.map((issue) => (
                        <IssueCard issue={issue} handleClickOpen={handleClickOpen}/>
                    ))}

                </Stack>
                <Box padding={1} display={"flex"} justifyContent="center" alignItems="center">
                    <Button color="secondary" onClick={handleClickOpenCreate}>+ 이슈 만들기</Button>
                </Box>
            </Box>
        </Grid>
        
    );
}

export default IssueList;