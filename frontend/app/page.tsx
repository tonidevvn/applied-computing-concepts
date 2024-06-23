'use client'
import { Input, Layout } from 'antd'
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

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/api/product')
                setItems(response.data)
            } catch (error) {
                console.error('Fetch error:', error)
            } finally {
                setLoading(false)
            }
        }

        fetchData()
    }, [])

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
                </div>
            </Content>
            <AppFooter />
        </Layout>
    )
}
