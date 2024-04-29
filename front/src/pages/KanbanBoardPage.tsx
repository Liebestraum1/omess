import {Button, Card, CardContent, Stack, Typography, Avatar, Chip, Container} from "@mui/material";
import Grid from '@mui/material/Unstable_Grid2';
import Box from "@mui/material/Box"; // Grid version 2

const KanbanBoardPage = () => {
    return (
        <Container style={{padding: 10}}>
            <Grid container columnSpacing={3}>
                {/** 진행전 */}
                <Grid xs={3.5} >
                    <Box style={{backgroundColor: "#F7F8F9"}} padding={1} height={"100%"}>
                        <Typography style={{padding: 10}}> 진행전 </Typography>
                        <Stack spacing={3} >
                            <Card>
                                <CardContent>
                                    <Grid container>
                                        <Grid xs={9}>
                                            <Typography style={{paddingBottom: 10}}> [Back-End] 회원 가입 api 구현</Typography>
                                            <Chip label="back-end" color="primary" />
                                        </Grid>
                                        <Grid xs={3} >
                                            <Box display={"flex"} flexDirection={"column"} justifyContent="center"  alignItems="center">
                                                <Avatar alt="프로필 이미지"
                                                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"/>
                                                <Box style={{paddingBottom: 20}}></Box>
                                                <Button  size="small">더보기</Button>
                                            </Box>
                                        </Grid>
                                    </Grid>
                                </CardContent>
                            </Card>
                        </Stack>
                        <Box padding={1} display={"flex"} justifyContent="center"  alignItems="center">
                            <Button>+ 이슈 만들기</Button>
                        </Box>
                    </Box>
                </Grid>
                {/** 진행 중 */}
                <Grid xs={3.5} >
                    <Box style={{backgroundColor: "#F7F8F9"}} padding={1} height={"100%"}>
                        <Typography style={{padding: 10}}> 진행전 </Typography>
                        <Stack spacing={3} >
                            <Card>
                                <CardContent>
                                    <Grid container>
                                        <Grid xs={9}>
                                            <Typography style={{paddingBottom: 10}}> [Back-End] 회원 가입 api 구현</Typography>
                                            <Chip label="back-end" color="primary" />
                                        </Grid>
                                        <Grid xs={3} >
                                            <Box display={"flex"} flexDirection={"column"} justifyContent="center"  alignItems="center">
                                                <Avatar alt="프로필 이미지"
                                                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"/>
                                                <Box style={{paddingBottom: 20}}></Box>
                                                <Button  size="small">더보기</Button>
                                            </Box>
                                        </Grid>
                                    </Grid>
                                </CardContent>
                            </Card>
                        </Stack>
                        <Box padding={1} display={"flex"} justifyContent="center"  alignItems="center">
                            <Button>+ 이슈 만들기</Button>
                        </Box>
                    </Box>
                </Grid>
                {/** 완료 */}
                <Grid xs={3.5} >
                    <Box style={{backgroundColor: "#F7F8F9"}} padding={1} height={"100%"}>
                        <Typography style={{padding: 10}}> 진행전 </Typography>
                        <Stack spacing={3} >
                            <Card>
                                <CardContent>
                                    <Grid container>
                                        <Grid xs={9}>
                                            <Typography style={{paddingBottom: 10}}> [Back-End] 회원 가입 api 구현</Typography>
                                            <Chip label="back-end" color="primary" />
                                        </Grid>
                                        <Grid xs={3} >
                                            <Box display={"flex"} flexDirection={"column"} justifyContent="center"  alignItems="center">
                                                <Avatar alt="프로필 이미지"
                                                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"/>
                                                <Box style={{paddingBottom: 20}}></Box>
                                                <Button  size="small">더보기</Button>
                                            </Box>
                                        </Grid>
                                    </Grid>
                                </CardContent>
                            </Card>
                            <Card>
                                <CardContent>
                                    <Grid container>
                                        <Grid xs={9}>
                                            <Typography style={{paddingBottom: 10}}> [Back-End] 회원 가입 api 구현</Typography>
                                            <Chip label="back-end" color="primary" />
                                        </Grid>
                                        <Grid xs={3} >
                                            <Box display={"flex"} flexDirection={"column"} justifyContent="center"  alignItems="center">
                                                <Avatar alt="프로필 이미지"
                                                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"/>
                                                <Box style={{paddingBottom: 20}}></Box>
                                                <Button  size="small">더보기</Button>
                                            </Box>
                                        </Grid>
                                    </Grid>
                                </CardContent>
                            </Card>
                            <Card>
                                <CardContent>
                                    <Grid container>
                                        <Grid xs={9}>
                                            <Typography style={{paddingBottom: 10}}> [Back-End] 회원 가입 api 구현</Typography>
                                            <Chip label="back-end" color="primary" />
                                        </Grid>
                                        <Grid xs={3} >
                                            <Box display={"flex"} flexDirection={"column"} justifyContent="center"  alignItems="center">
                                                <Avatar alt="프로필 이미지"
                                                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"/>
                                                <Box style={{paddingBottom: 20}}></Box>
                                                <Button  size="small">더보기</Button>
                                            </Box>
                                        </Grid>
                                    </Grid>
                                </CardContent>
                            </Card>
                        </Stack>
                        <Box padding={1} display={"flex"} justifyContent="center"  alignItems="center">
                            <Button>+ 이슈 만들기</Button>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </Container>

    );
}

export default KanbanBoardPage;