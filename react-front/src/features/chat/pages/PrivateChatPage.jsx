import { useState } from "react";
import useWebsocket from "../hooks/useWebsocket";
import styles from './ChatPage.module.css';
import Button from "../../../components/commons/ui/Button";
import { useAuth } from "../../../context/AuthContext";

//개인채팅도 가능하도록 구현해보기 (연결 및 확인 해보세요)
function PrivateChatPage(){
    
    //const url = 'ws://localhost:8080/spring/group';
    const [inputMessage,setInputMessage] = useState('');
    const {user} = useAuth(); //로그인 정보 가져오기
    const [targetUserId, setTargetUserId] = useState(''); //상대방 아이디 
    const [selectedUser,setSelectedUser] = useState(null);//선택된 대상 

    
    //토큰 추출하기
    const token = localStorage.getItem('token');
    
    const {isConnected,message,connect,disconnect,sendMessage,userList} = useWebsocket(`/private?token=${token}`);


    //유저 선택 
    const handleUserSelect = (userId) =>{
        if(userId === user?.userId){
            alert('자신에게 귓속말을 할 수 없습니다.');
            return;
        }

        setSelectedUser(userId);
        setTargetUserId(userId);
    };

    //귓속말 대상 해제 
    const handleClearTarget = () =>{
        setSelectedUser(null);
        setTargetUserId('');
    };

    const handleSend = () =>{
        //공백만 있을경우 처리 
        if(!inputMessage.trim()) return;

        const messageData = {
            message : inputMessage,
            myId : user?.userId,
            otherId : targetUserId,
            type : targetUserId ? 2 : 1, //2번 귓속말 1번 전체
        };


        sendMessage(messageData); //메시지 전달함수에 입력값 넣기 
        setInputMessage(''); //전송했으니 빈칸 만들기 
    };

    const handleKeyDown = (e) =>{
        if(e.key === 'Enter'){
            handleSend(); //엔터키 누르면 메시지 전송하기 
        }

    }

    return (
        <div className='container'>
            <div className={styles.wrapper}>
                <h2 className={styles.title}>개인 채팅 페이지</h2>


                <div className={styles.controls}>
                    <Button
                     variant="primary"
                     onClick={connect}
                     disabled={isConnected}
                    >
                    접속
                    </Button>
                    <Button
                        variant='danger'
                        onClick={disconnect}
                        disabled={!isConnected}
                    >
                        접속종료
                    </Button>
                    <span className={`${styles.stauts}${isConnected?styles.connected: ''}`}>
                        {isConnected? '🟢 연결됨' : '⭕ 연결안됨'}
                    </span>
                </div>

                <div className={styles.inputArea}>
                    <input type="text" 
                           value={inputMessage}
                           onChange={(e)=>setInputMessage(e.target.value)}
                           placeholder="메시지를 입력하세요"
                           className={styles.input}
                           disabled={!isConnected}
                           onKeyDown={handleKeyDown}
                    />
                    <Button variant="secondary" onClick={handleSend} disabled={!isConnected}>
                        전송
                    </Button>
                </div>

                {/* 채팅영역 */}
                <div className={styles.chatArea}>
                    {message.map((msg,index)=>(
                        <div key={index} className={`${styles.message} ${msg.type === 2 ? styles.privateMessage:''}`}>
                           [{msg.time||''}] {msg.message} - {msg.myId}
                        </div>    
                    ))
                    }
                </div>
                
                {
                    selectedUser && (
                        <div className={styles.targetInfo} onClick={handleClearTarget}>
                            귓속말 선택된 대상 : <strong>{selectedUser}</strong>
                            <span className={styles.clearTarget}>클릭하면 해제됨</span>
                        </div>
                    )
                }

                {/* 유저 목록 출력 */}
                <div className={styles.userListarea}>
                    <h4 className={styles.userListTitle}>접속자 목록</h4>
                    <ul className={styles.userList}>
                        {
                            userList.map((userId,index)=>(
                                <li key={index}
                                    className={`${styles.userItem} ${userId===selectedUser ? styles.selected : ''} 
                                                ${userId===user?.userId?styles.me:''}`}
                                    onClick={()=>handleUserSelect(userId)}                                                
                                                >
                                    {userId}
                                    {userId ===user?.userId && ' (본인)'}
                                </li>
                            ))
                        }
                    </ul>
                </div>
            </div>
        </div>
    );
}
export default PrivateChatPage;