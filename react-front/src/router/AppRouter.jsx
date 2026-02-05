import { Route, Routes } from "react-router-dom";
import RegisterPage from "../features/auth/pages/RegisterPage";
import MyPage from "../features/auth/pages/MyPage";
import BoardListPage from "../features/board/pages/BoardListPage";
import BoardDetailPage from "../features/board/pages/BoardDetailPage";
import HomePage from "../features/board/pages/HomePage";
import PrivateRoute, { PublicRoute } from "./PrivateRouter";
import BoardWritePage from "../features/board/pages/BoardWritePage";
import ChatSelectPage from "../features/chat/pages/ChatSelectPage";
import BasicChatPage from "../features/chat/pages/BasicChatPage";
import GroupChatPage from "../features/chat/pages/GroupChatPage";
import MemberChatPage from "../features/chat/pages/MemberChatPage";
import PrivateChatPage from "../features/chat/pages/PrivateChatPage";


function AppRouter() {
    return (
        <Routes>

            {/* 메인 */}
            <Route path="/" element={<HomePage/>}></Route>
            {/* 인증  */}
            <Route path="/register" element={
                 <PublicRoute>
                    <RegisterPage/>
                 </PublicRoute>
                }/>
            <Route path="/mypage" element={
                        <PrivateRoute>
                            <MyPage/>
                        </PrivateRoute>
                        }/>
            


            {/* 자유게시판 */}
            <Route path="/board" element={<BoardListPage/>}/>
            <Route path="/board/:boardNo" element={<BoardDetailPage/>}/>

            <Route path="/board/write" element={
                 <PrivateRoute>
                     <BoardWritePage/>
                 </PrivateRoute>  
             }/>

            <Route path="/board/:boardNo/edit" element={
                 <PrivateRoute>
                     <BoardWritePage/>
                 </PrivateRoute>  
            }/>

            {/* 채팅 */}
            <Route path="/chat" element={<ChatSelectPage/>}></Route>
            <Route path="/chat/basic" element={<BasicChatPage/>}></Route>
            <Route path="/chat/group" element={<GroupChatPage/>}></Route>
            <Route path="/chat/member" element={ <PrivateRoute>
                                                    <MemberChatPage/>
                                                 </PrivateRoute>
                                                }></Route>
             <Route path="/chat/private" element={ <PrivateRoute>
                                                    <PrivateChatPage/>
                                                 </PrivateRoute>
                                                }></Route>


        </Routes>

    )


}

export default AppRouter;
