import {Badge, Button, Card, Drawer, FloatButton, List, Space, Typography} from 'antd'
import axios from 'axios'
import { useEffect, useState } from 'react'
import {SearchDataType} from "@/app/types/searchdata";
import {QuestionCircleOutlined} from "@ant-design/icons";

export default function AppKeywordSearchHistory({
                                             queryValue
                                         }: {
    queryValue: string
}) {
    const [open, setOpen] = useState(false);

    const showDrawer = () => {
        setOpen(true);
    };

    const onClose = () => {
        setOpen(false);
    };

    const [topSearches, setTopSearches] = useState<SearchDataType[]>([])
    const [recentSearches, setRecentSearches] = useState<SearchDataType[]>([])

    useEffect(() => {
        const fetchSearchHistoryData = async () => {
            try {
                if (!!queryValue) {
                    const response = await axios.get('/api/keyword-search', {
                        params: { q: queryValue },
                    })
                    const response2 = await axios.get('/api/keyword-search/list', {
                        params: { q: 'top' },
                    })
                    const response3 = await axios.get('/api/keyword-search/list', {
                        params: { q: 'recent' },
                    })
                    if (!!response.data) {
                        setTopSearches(response2.data)
                        setRecentSearches(response3.data)
                    }
                }
            } catch (error) {
                console.error('Fetch error:', error)
            }
        }
        fetchSearchHistoryData()
    }, [queryValue])
    return (
        <>
            <FloatButton icon={<QuestionCircleOutlined />} type="primary" style={{ right: 24 }} onClick={showDrawer} />
            <Drawer title="Search History" onClose={onClose} open={open}>
                {!!topSearches && topSearches.length > 0 && (<Card title='Top Search'>
                        <Space size={'middle'} wrap>
                        {topSearches.map((keywordData: SearchDataType, i: number) => (
                            <Badge key={i} count={keywordData.count}>
                                <Button>
                                    {keywordData.keyword}
                                </Button>
                            </Badge>
                        ))}
                        </Space>
                </Card>
                    )}

                {!!recentSearches && recentSearches.length > 0 && (<Card title='Search History'>
                        <Space size="middle">
                            <List
                                className="min-w-[120px]"
                                dataSource={recentSearches}
                                renderItem={(keywordData, index) => (
                                    <List.Item>
                                        [#{index}] <Typography.Text mark>{keywordData.keyword}</Typography.Text>
                                    </List.Item>
                                )}
                            />
                        </Space>
                    </Card>
                )}
            </Drawer>
        </>
    )
}
