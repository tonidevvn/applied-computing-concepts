'use client'

import { FoodItemType } from '@/app/types/food'
import Image from 'next/image'
import React, {useEffect, useState} from 'react'
import {Card, Row, Col, PaginationProps, Flex, Pagination} from 'antd'
import axios from "axios";
import AppAutoComplete from "@/app/components/AppAutoComplete";
import AppSpellChecking from "@/app/components/AppSpellChecking";

const { Meta: CardMeta } = Card

// Utility function to truncate text
const truncateText = (text: string, maxLength: number) => {
    if (!text) {
        return ''
    }
    if (text.length <= maxLength) {
        return text
    }
    return text.slice(0, maxLength) + '...'
}

function FoodItem({ items }: { items: FoodItemType[] }) {
    return (
        <Row gutter={[4, 4]} justify='center'>
            {!!items && items.map((item, index) => (
                <Col key={index} xs={24} sm={12} md={8} lg={6} xl={4}>
                    <Card
                        hoverable
                        style={{ width: '100%', height: '100%' }}
                        cover={
                            <div
                                style={{
                                    height: 200,
                                    overflow: 'hidden',
                                    position: 'relative',
                                }}
                            >
                                <Image
                                    src={item.image}
                                    alt={item.name}
                                    layout='fill'
                                    objectFit='cover'
                                />
                            </div>
                        }
                    >
                        <CardMeta
                            title={item.name}
                            description={truncateText(item.description, 100)} // Adjust the maxLength as needed
                        />
                        <div style={{ marginTop: 16, textAlign: 'center' }}>
                            <p
                                style={{
                                    fontWeight: 'bold',
                                    fontSize: '1.2em',
                                }}
                            >
                                {item.price}
                            </p>
                        </div>
                    </Card>
                </Col>
            ))}
        </Row>
    )
}

export default function Products() {

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
                setItems(response.data)
                setTotal(response.data.length)
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
        <Flex gap={'middle'} align='center' vertical={true}>
            <Flex vertical={true}>
                <AppAutoComplete
                    searchValue={searchValue}
                    setSearchValue={setSearchValue}
                />
                <AppSpellChecking
                    searchValue={searchValue}
                    setSearchValue={setSearchValue}
                />
            </Flex>
            <Flex gap='middle' vertical={true}>
                <FoodItem items={items} />
                <Pagination
                    style={{ marginLeft: 'auto' }}
                    showSizeChanger
                    onChange={onPaginationChange}
                    defaultCurrent={page}
                    total={total}
                />
            </Flex>
        </Flex>
    )
}
