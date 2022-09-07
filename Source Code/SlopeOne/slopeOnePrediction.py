"""
@author:Zhao
@ide:PyCharm
@createTime:2018-11-21
"""
import numpy as np


# 计算出两个项目之间偏差的平均值
def getTwoItemDev(item1, item2):
    (item1_,) = np.where(item1 > 0)
    (item2_,) = np.where(item2 > 0)
    commonIndex = np.sort(list(set(item1_).intersection(set(item2_))))
    dev = 0
    if len(commonIndex) > 0:
        for i in commonIndex:
            dev += item1[i] - item2[i]
        # dev = np.sum(item1[commonIndex] - item2[commonIndex]) # 用时更长
        return dev / len(commonIndex), len(commonIndex)
    else:
        return 0, 0


# 任意两个item的dev和commonIndexSize
def getItemDevMatrix(matrixTranspose, numService):
    itemDevMatrix = np.zeros((numService, numService), np.float)
    commonIndexSizeMatrix = np.zeros((numService, numService), np.float)
    for i in range(numService):
        for j in range(i + 1, numService):
            dev_item, common_index_size = getTwoItemDev(matrixTranspose[i], matrixTranspose[j])
            itemDevMatrix[i][j] = dev_item
            itemDevMatrix[j][i] = -dev_item  # 注意此处是负号，有方向的减
            commonIndexSizeMatrix[i][j] = common_index_size
            commonIndexSizeMatrix[j][i] = common_index_size
    return itemDevMatrix, commonIndexSizeMatrix


# 为第i个用户的第j个服务做预测
def predict_value(matrix, matrixTranspose, i, j, list_item_mean, numService):
    cji = 0  # 权重，共同调用的个数和来衡量
    predict_val = 0
    for item in range(numService):
        if matrix[i][item] > 0:
            dev_item_j, common_index_size = getTwoItemDev(matrixTranspose[j], matrixTranspose[item])
            cji += common_index_size
            predict_val += (dev_item_j + matrix[i][item]) * common_index_size

    if cji > 0:
        predict_val = predict_val / cji
    else:
        return list_item_mean[j]
    if predict_val > 0:
        return predict_val
    else:
        return list_item_mean[j]


# 为第i个用户的第j个服务做预测
def predict_value2(matrix, i, j, list_item_mean, numService, itemDevMatrix, commonIndexSizeMatrix):
    cji = 0  # 权重，共同调用的个数和来衡量
    predict_val = 0
    for item in range(numService):
        if matrix[i][item] > 0:
            dev_item_j = itemDevMatrix[j][item]
            common_index_size = commonIndexSizeMatrix[j][item]
            cji += common_index_size
            predict_val += (dev_item_j + matrix[i][item]) * common_index_size

    if cji > 0:
        predict_val = predict_val / cji
    else:
        return list_item_mean[j]
    if predict_val > 0:
        return predict_val
    else:
        return list_item_mean[j]

# 为某个用户做预测
# def predict_for_one_user(removedMatrix, removedMatrixItem, i, list_item_mean, numUser, numService):
#     predict_list = np.zeros(numService, np.float)
#     for j in range(numService):
#         if removedMatrix[i][j] > 0:
#             continue
#         else:
#             predict_list[j] = predict_value(removedMatrix, removedMatrixItem, i, j, list_item_mean, numUser, numService)
#     return predict_list


def predict_for_all_user(originalMatrix, removedMatrix, removedMatrixTranspose, itemMeanList, numUser,
                         numService):
    predictMatrix = np.zeros_like(removedMatrix, np.float)
    for i in range(numUser):
        for j in range(numService):
            if removedMatrix[i][j] > 0 or originalMatrix[i][j] < 0:
                continue
            else:
                predictMatrix[i][j] = predict_value(removedMatrix, removedMatrixTranspose, i, j, itemMeanList,
                                                    numService)
    return predictMatrix


# 2是1的优化版
def predict_for_all_user2(originalMatrix, removedMatrix, removedMatrixItem, list_item_mean, numUser,
                          numService):
    predictMatrix = np.zeros_like(removedMatrix, np.float)
    itemDevMatrix, commonIndexSizeMatrix = getItemDevMatrix(removedMatrixItem, numService)
    for i in range(numUser):
        for j in range(numService):
            if removedMatrix[i][j] > 0 or originalMatrix[i][j] < 0:
                continue
            else:
                predictMatrix[i][j] = predict_value2(removedMatrix, i, j, list_item_mean,
                                                     numService, itemDevMatrix, commonIndexSizeMatrix)
    return predictMatrix
