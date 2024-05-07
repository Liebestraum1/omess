// import axios from "axios";
// import {KanbanBoardProp} from "../../types/KanbanBoard/KanbanBoard.ts";
// import {IssueProp} from "../../types/Issue/Issue.ts";
// import {LabelProp} from "../../types/Label/Label.ts";
// import {CreateIssueProp} from "../../types/Issue/CreateIssue.ts";
//
// export const getKanbanBoard = async <T = KanbanBoardProp>(projectId: number, moduleId: number):
//     Promise<T> => {
//     return await axios.get(`http://localhost:8080/projects/${projectId}/kanbanboards/${moduleId}`);
// }
//
// export const getIssues = async <T = IssueProp[]>
// (projectId: number, moduleId: number, chargerId: number | null,
//  labelId: number | null, importance: number | null): Promise<T> => {
//     return await axios.get(`http://localhost:8080/projects/${projectId}/kanbanboards/${moduleId}/issues`,
//         {
//             params: {
//                 chargerId: chargerId,
//                 labelId: labelId,
//                 importance: importance
//             }
//         }
//     );
// }
//
// export const getLabels = async <T = LabelProp[]>(
//     projectId: number, moduleId: number): Promise<T> => {
//     return await axios.get(`http://localhost:8080/projects/${projectId}/kanbanboards/${moduleId}/label`);
// }
//
// export const createIssue = async (projectId: number, moduleId: number, writeIssueRequest: CreateIssueProp) => {
//     return await axios.post(`http://localhost:8080/projects/${projectId}/kanbanboards/${moduleId}/label`,
//         {
//             writeIssueRequest: writeIssueRequest
//         }
//     );
// }