import matplotlib.pyplot as plt
import pandas as pd
import matplotlib as mpl
import os, sys
import ast
from matplotlib.pyplot import MultipleLocator


def draw_linechart(x_name, x_values, y_name, y_legends, y_values, save_file):
    mpl.rcParams['axes.linewidth'] = 1.2
    plt.rc('font', family='Helvetica', size=10, weight='roman')
    plt.rc('pdf', fonttype=42)
    # plt.figure(figsize=(4.0, 2.5))
    # plt.subplots_adjust(
    #     left=0.12,
    #     bottom=0.18,
    #     right=0.96,
    #     top=0.94,
    #     wspace=0.00,
    #     hspace=0.00)
    # plt.rcParams['xtick.direction'] = 'in'
    # plt.rcParams['ytick.direction'] = 'in'

    plt.xlabel(x_name)
    plt.ylabel(y_name)

    y_min = min(y_values)
    y_max = max(y_values)
    if y_max - y_min <= 50:
        plt.ylim(y_min - 10, y_min + 50 + 10)

    # y_major_locator = MultipleLocator(50)
    # plt.gca().yaxis.set_major_locator(y_major_locator)

# plt.ylim(0.3, 1.05)
    # plt.xlim(0, 600)
    # marksize = 2
    # linewidth = 1

    plt.plot(x_values, y_values)

    plt.legend(labels=y_legends)
    # plt.show()
    plt.savefig(save_file)


def main():

    x_name = sys.argv[1]
    print x_name

    x_values = [int(e) for e in sys.argv[2].split(",")]
    print x_values

    y_name = sys.argv[3]
    print y_name

    y_legends = sys.argv[4].split(",")
    print y_legends

    y_values = [float(e) for e in sys.argv[5].split(",")]
    print y_values

    save_file = sys.argv[6]
    print save_file

    draw_linechart(x_name, x_values, y_name, y_legends, y_values, save_file)


if __name__ == '__main__':
    sys.exit(main())
