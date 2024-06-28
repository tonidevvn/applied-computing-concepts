'use client'
import { Input, Layout, Pagination, PaginationProps } from 'antd'
import { Content } from 'antd/lib/layout/layout'
import AppHeader from './components/AppHeader'
import AppFooter from './components/AppFooter'
import FoodItem from './components/Products'
import { useEffect, useState } from 'react'
import axios from 'axios'
import { FoodItemType } from './types/food'
import AppAutoComplete from './components/AppAutoComplete'

export default function Home() {
    const [items, setItems] = useState<FoodItemType[]>([])
    const [loading, setLoading] = useState(true)
    const [searchValue, setSearchValue] = useState('')
    const [page, setPage] = useState(0)
    const [limit, setLimit] = useState(10)
    const [total, setTotal] = useState(0)

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/api/product', {
                    params: {
                        keyword: searchValue,
                        page,
                        limit,
                    },
                })
                setItems(response.data.data)
                setTotal(response.data.total)
            } catch (error) {
                console.error('Fetch error:', error)
            } finally {
                setLoading(false)
            }
        }

        fetchData()
    }, [searchValue, page, limit])

    const onPaginationChange: PaginationProps['onShowSizeChange'] = (
        current,
        pageSize
    ) => {
        setPage(current)
        setLimit(pageSize)
    }

    return (
        <Layout>
            <AppHeader />
            <Content style={{ padding: '0 48px' }}>
                <div
                    style={{
                        background: '#fff',
                        minHeight: '100vh',
                        padding: 24,
                        borderRadius: '20px',
                    }}
                >
                    <AppAutoComplete
                        searchValue={searchValue}
                        setSearchValue={setSearchValue}
                    />
                    <FoodItem items={items} />
                    <Pagination
                        showSizeChanger
                        onChange={onPaginationChange}
                        defaultCurrent={page}
                        total={total}
                    />
                </div>
            </Content>
            <AppFooter />
        </Layout>
    )
}
