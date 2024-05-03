import {useEffect, useState} from "react";
import {Box, List, ListItem} from "@mui/material";
import {loadApiSpecification} from "./request/ApiSpecificationRequest.ts";
import {ApiSpecification} from "../../types/api-specification/ApiSpecification.ts";
import ApiListComponent from "./api/ApiListComponent.tsx";
import DomainComponent from "./domain/DomainComponent.tsx";

const ApiSpecificationComponent = ({projectId, apiSpecificationId}: {
    projectId: number,
    apiSpecificationId: number
}) => {
    const [ApiSpecification, setApiSpecification] = useState<ApiSpecification | null>(null);
    const fetchApiSpecification =  async () => {
        const data = await loadApiSpecification(projectId, apiSpecificationId);
        setApiSpecification(data);
    }

    const handleChildChange = () => {
        fetchApiSpecification();
    };


    useEffect(() => {
        fetchApiSpecification()
    }, [projectId, apiSpecificationId]);

    return ApiSpecification ? (
        <>

            {/* Header */}
            <Box
                width="100%"
                height="100%"
                sx={{border: '2px solid black'}}
            >
                {ApiSpecification.domains.map((domain) => (
                    <List key={`domain-${domain.domainId}`}>
                        <ListItem
                            sx={{
                                // border: '2px solid blue',
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'space-between',
                            }}
                        >
                            <DomainComponent
                                projectId={projectId}
                                apiSpecificationId={apiSpecificationId}
                                domainId={domain.domainId}
                                name={domain.name}
                                onChildChange={handleChildChange}
                            />
                            <ApiListComponent apis={domain.apis}/>
                        </ListItem>
                    </List>
                ))}
            </Box>
        </>
    ) : null;
}

export default ApiSpecificationComponent;
