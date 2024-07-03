import {Badge, Button, Card, Space} from 'antd'
import axios from 'axios'
import { useEffect, useState } from 'react'
import {SearchDataType} from "@/app/types/searchdata";

export default function AppSearchHistory({
                                             queryValue
                                         }: {
    queryValue: string
}) {
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
        {!!topSearches && topSearches.length > 0 && (<Card title='Top Search'>
                <Space size="middle">
                {topSearches.map((keywordData: SearchDataType, i:number) => (
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
            {recentSearches.map((keywordData: SearchDataType, i:number) => (
                    <Button key={i} >
                        {keywordData.keyword}
                    </Button>
            ))}
            </Space>
            </Card>
        )}
        </>
    )
}
